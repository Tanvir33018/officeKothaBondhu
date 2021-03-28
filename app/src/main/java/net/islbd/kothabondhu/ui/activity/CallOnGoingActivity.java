 package net.islbd.kothabondhu.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import net.islbd.kothabondhu.BuildConfig;
import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.document.MyVariableStore;
import net.islbd.kothabondhu.model.pojo.CallHistoryDetails;
import net.islbd.kothabondhu.model.pojo.MyDuration;
import net.islbd.kothabondhu.model.pojo.StatusInfo;
import net.islbd.kothabondhu.model.pojo.UserDuration;
import net.islbd.kothabondhu.model.pojo.UserGmailInfo;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.service.SinchService;
import net.islbd.kothabondhu.utility.AudioPlayer;
import net.islbd.kothabondhu.utility.GlobalConstants;
import net.islbd.kothabondhu.utility.HttpStatusCodes;

import retrofit2.Response;

public class CallOnGoingActivity extends BaseActivity implements SensorEventListener {
    static final String TAG = CallOnGoingActivity.class.getSimpleName();

    private AudioPlayer mAudioPlayer;
    private Timer mTimer;
    private String mCallId, mAgentPhotoUrl;
    private long mCallStart = 0;
    private SharedPreferences sharedPreferences;
    private ImageView callDismissImageView, micImageView, speakerImageView;
    private CircularImageView userImageView, agentImageView;
    private TextView callStateTextView, agentname;
    private Chronometer callStateChronoMeter;
    private retrofit2.Call<StatusInfo> callToSetHistory;
    private IApiInteractor apiInteractor;

    private SensorManager sensorManager;
    private Sensor proximity;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private int field = 0x00000020;

    private AudioManager audioManager;

    private boolean isMicOn = true, isSpeakerOn = false;

    private CallHistoryDetails callHistoryDetails;

    private UserDuration userDuration;

    private AppPresenter appPresenter;

    private Thread limitControlThread;

    private boolean threadIsRunning;
    private String agentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.NoActionBar);
        setContentView(R.layout.activity_call_on_going);
        //balanceCheckingWork();
        initAll();
    }

    private void initAll(){
        initializeWidgets();
        initializeData();
        eventListeners();
    }

    private void balanceCheckingWork(){
        String id = getUserInfoFromGMail().getId();
        userDuration = new UserDuration(id);
        retrofit2.Call<MyDuration> myDurationCall = apiInteractor.getMyDuration(userDuration);
        myDurationCall.enqueue(new retrofit2.Callback<MyDuration>() {
            @Override
            public void onResponse(retrofit2.Call<MyDuration> call, Response<MyDuration> response) {
                if(response.isSuccessful() && response.body() != null){
                    MyDuration myDuration = response.body();
                    Toast.makeText(getApplicationContext(), "Remaining Balance" + myDuration.getDuration(), Toast.LENGTH_SHORT).show();
                    double duration = Double.parseDouble(myDuration.getDuration());
                    if(duration <= 0.0){
                        Toast.makeText(getApplicationContext(), "You do not have sufficient balance", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else initAll();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Some problem occurs", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<MyDuration> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Balance check failed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void initializeWidgets() {
        callDismissImageView = findViewById(R.id.call_activity_dismiss_imageview);
        callStateTextView = findViewById(R.id.call_state_textView);
        agentname = findViewById(R.id.agentNameTextView);
        userImageView = findViewById(R.id.call_contact_imageView);
        agentImageView = findViewById(R.id.call_agent_imageView);
        callStateChronoMeter = findViewById(R.id.call_state_chronoMeter);
        micImageView = findViewById(R.id.call_activity_mic_imageview);
        speakerImageView = findViewById(R.id.call_activity_speaker_imageview);
    }

    private void initializeData() {
        isMicOn = false;
        isSpeakerOn = false;

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximity = sensorManager != null ? sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) : null;

        try {
            // Yeah, this is hidden field.
            field = PowerManager.class.getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);
        } catch (Throwable ignored) {
        }

        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager != null ? powerManager.newWakeLock(field, getLocalClassName()) : null;

        appPresenter = new AppPresenter();
        apiInteractor = appPresenter.getApiInterface();
        sharedPreferences = appPresenter.getSharedPrefInterface(this);

        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
        try {
            mAgentPhotoUrl = getIntent().getStringExtra(GlobalConstants.EXT_TAG_URL);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        mAudioPlayer = new AudioPlayer(this);
        mCallStart = System.currentTimeMillis();
        sharedPreferences = new AppPresenter().getSharedPrefInterface(this);
        //agentName = sharedPreferences.getString(GlobalConstants.EXT_TAG_NAME,"No Name");
        agentname.setText(sharedPreferences.getString(GlobalConstants.EXT_TAG_NAME,"No Name"));

        if (mAgentPhotoUrl != null) {
            loadImage(mAgentPhotoUrl, agentImageView);
        }

    }

    private void eventListeners() {
        callDismissImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endCall();
            }
        });

        micImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMic();
            }
        });

        speakerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSpeaker();
            }
        });
    }

    private void toggleMic() {
        if (audioManager == null) {
            Toast.makeText(CallOnGoingActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            return;
        }

        audioManager.setMode(AudioManager.MODE_IN_CALL);

        if (isMicOn) {
            micImageView.setImageResource(R.drawable.ic_mic_off);
            audioManager.setMicrophoneMute(true);
        } else {
            micImageView.setImageResource(R.drawable.ic_mic_on);
            audioManager.setMicrophoneMute(false);
        }

        isMicOn = !isMicOn;
    }

    private void toggleSpeaker() {
        if (audioManager == null) {
            Toast.makeText(CallOnGoingActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            return;
        }

        audioManager.setMode(AudioManager.MODE_IN_CALL);

        if (isSpeakerOn) {
            speakerImageView.setImageResource(R.drawable.ic_speaker_off);
            audioManager.setSpeakerphoneOn(false);
        } else {
            speakerImageView.setImageResource(R.drawable.ic_speaker_on);
            audioManager.setSpeakerphoneOn(true);
        }

        isSpeakerOn = !isSpeakerOn;
    }

    private void initializeMicAndSpeaker() {
        isSpeakerOn = false;
        isMicOn = true;

        if (audioManager == null) {
            Toast.makeText(CallOnGoingActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            return;
        }

        audioManager.setMode(AudioManager.MODE_IN_CALL);
        audioManager.setMicrophoneMute(!isMicOn);
        audioManager.setSpeakerphoneOn(isSpeakerOn);
        micImageView.setImageResource(R.drawable.ic_mic_on);
        speakerImageView.setImageResource(R.drawable.ic_speaker_off);
    }

    private void loadImage(String url, ImageView imageView) {
        Picasso picasso = Picasso.get();
        //picasso.setDebugging(true);
        picasso.load(url).error(R.drawable.ic_person).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
        } else {
            Log.e(TAG, "Started with invalid callId, aborting.");
            goBack();
        }
    }

    private void endCall() {
        threadIsRunning = false;
        mAudioPlayer.stopProgressTone();
        try{
            Call call = getSinchServiceInterface().getCall(mCallId);
            if (call != null) {
                call.hangup();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Exception occurs", Toast.LENGTH_SHORT).show();
        }
        goBack();
    }

    private void goBack() {
        finish();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float distance = sensorEvent.values[0];
        if (distance == 0.0) {
            if (!wakeLock.isHeld()) {
                wakeLock.acquire();
            }
        } else {
            if (wakeLock.isHeld()) {
                wakeLock.release();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        endCall();
    }

    private class SinchCallListener implements CallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended. Reason: " + cause.toString());
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            //String endMsg = "Call ended: " + call.getDetails().toString();
            //Toast.makeText(CallOnGoingActivity.this, endMsg, Toast.LENGTH_LONG).show();
            callStateChronoMeter.stop();
            callStateChronoMeter.setVisibility(View.GONE);
            callStateTextView.setVisibility(View.VISIBLE);
            if (BuildConfig.TYPE.equals(GlobalConstants.TYPE_USER)) postCallHistory(call);
            endCall();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            mCallStart = System.currentTimeMillis();
            //callStateTextView.setText(String.valueOf(mCallStart));
            callStateTextView.setVisibility(View.GONE);
            callStateChronoMeter.setVisibility(View.VISIBLE);
            callStateChronoMeter.setBase(SystemClock.elapsedRealtime());
            callStateChronoMeter.start();
            if (BuildConfig.TYPE.equals(GlobalConstants.TYPE_USER)) {
                threadIsRunning = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        double remaining = getIntent().getDoubleExtra(GlobalConstants.REMAINING_DURATION, 0);
                        long remainInMillisecond = (long)remaining * 1000 * 60;
                        //long remainInMillisecond = 10000;
                        Log.d(TAG, "Call established" + remainInMillisecond);
                        while(remainInMillisecond != 0){
                            if(threadIsRunning){
                                try{ Thread.sleep(remainInMillisecond); }
                                catch (Exception e){ }
                                remainInMillisecond = 0;
                            }
                            else return;
                        }
                        if(remainInMillisecond == 0){
                            Log.d(TAG, "Call establishedxx" + remainInMillisecond);
                            MyVariableStore.balance_empty = true;
                            endCall();
                        }
                    }
                }).start();
            }
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
            callStateTextView.setText("Contacting.....");
            //endCall();
            mAudioPlayer.playProgressTone();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }
    }

    private void postCallHistory(Call call) {
        Date date = new Date(TimeUnit.SECONDS.toMillis(call.getDetails().getStartedTime()));
        callHistoryDetails = new CallHistoryDetails();
        //callHistoryDetails.setAgentId(call.getCallId());
        callHistoryDetails.setAgentId(getIntent().getStringExtra(GlobalConstants.F_CALL_ID));
        callHistoryDetails.setCallDate(date.toString());
        String duration = String.valueOf(call.getDetails().getDuration());
        callHistoryDetails.setDuration(duration);
        SharedPreferences sharedPreferences = getSharedPreferences("app_settings_mine", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEdit = sharedPreferences.edit();
        sharedPreferencesEdit.putString("call_duration", duration);
        sharedPreferencesEdit.apply();
        UserGmailInfo userGmailInfo = getUserInfoFromGMail();
        //callHistoryDetails.setUserId(String.valueOf(sharedPreferences.getInt(SharedPrefUtils._USER_PHONE, 0)));
        callHistoryDetails.setUserId(userGmailInfo.getId());
        callToSetHistory = apiInteractor.setCallHistory(callHistoryDetails);
        callToSetHistory.enqueue(new retrofit2.Callback<StatusInfo>() {
            @Override
            public void onResponse(retrofit2.Call<StatusInfo> call, Response<StatusInfo> response) {
                if (response.code() == HttpStatusCodes.OK) {
                    if (response.body() != null) {
                        StatusInfo statusInfo = response.body();

                        if(MyVariableStore.balance_empty){
                            Toast.makeText(getApplicationContext(), "YOU DO NOT HAVE SUFFICIENT BALANCE!", Toast.LENGTH_LONG).show();
                            MyVariableStore.balance_empty = false;
                        }
                        //else Toast.makeText(CallOnGoingActivity.this, statusInfo.getDescrption(), Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<StatusInfo> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Post failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private UserGmailInfo getUserInfoFromGMail(){
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this); //It will return null on sign out condition
        if(googleSignInAccount != null){
            String name = googleSignInAccount.getDisplayName();
            String email = googleSignInAccount.getEmail();
            String id = googleSignInAccount.getId();
            return new UserGmailInfo(name, email, id);
            //Log.d(TAG, "onCreate: " + email);
        }
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();
        mTimer.cancel();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        callStateChronoMeter.setVisibility(View.GONE);
        callStateTextView.setVisibility(View.VISIBLE);
        initializeMicAndSpeaker();

    }

    @Override
    public void onResume() {
        super.onResume();
        mTimer = new Timer();
        if (sensorManager != null) {
            sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
        callStateChronoMeter.setVisibility(View.GONE);
        callStateTextView.setVisibility(View.VISIBLE);
        initializeMicAndSpeaker();
    }
}
