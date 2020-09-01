package net.islbd.kothabondhu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.service.SinchService;
import net.islbd.kothabondhu.utility.AudioPlayer;

public class CallIncomingActivity extends BaseActivity {
    static final String TAG = CallIncomingActivity.class.getSimpleName();
    private String mCallId;
    private String mCallLocation;
    private AudioPlayer mAudioPlayer;
    private Context context;
    private ImageView callDismissImageView, callAcceptImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.NoActionBar);
        setContentView(R.layout.activity_call_incoming);

        initializeWidgets();
        initializeData();
        eventListeners();
    }

    private void initializeWidgets() {
        callDismissImageView = findViewById(R.id.call_dismiss);
        callAcceptImageView = findViewById(R.id.call_accept);
    }

    private void initializeData() {
        context = this;
        mAudioPlayer = new AudioPlayer(this);
        mAudioPlayer.playRingtone();
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
        mCallLocation = getIntent().getStringExtra(SinchService.LOCATION);
    }

    private void eventListeners() {
        callDismissImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineClicked();
            }
        });

        callAcceptImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerClicked();
            }
        });
    }

    @Override
    protected void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
        } else {
            Log.e(TAG, "Started with invalid callId, aborting");
            finish();
        }
    }

    private void answerClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.answer();
            Intent intent = new Intent(this, CallOnGoingActivity.class);
            intent.putExtra(SinchService.CALL_ID, mCallId);
            startActivity(intent);
        } else {
            finish();
        }
    }

    private void declineClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private class SinchCallListener implements CallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Toast.makeText(CallIncomingActivity.this, "call ended " + cause.toString(), Toast.LENGTH_SHORT).show();
            mAudioPlayer.stopRingtone();
            finish();
        }

        @Override
        public void onCallEstablished(Call call) {
            Toast.makeText(CallIncomingActivity.this, "connected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCallProgressing(Call call) {
            Toast.makeText(CallIncomingActivity.this, "progressing", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }
    }
}
