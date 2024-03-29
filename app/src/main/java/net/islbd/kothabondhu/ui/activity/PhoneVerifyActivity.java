package net.islbd.kothabondhu.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rbddevs.splashy.Splashy;
import com.sinch.android.rtc.SinchError;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import net.islbd.kothabondhu.BuildConfig;
import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.document.DocumentActivity;
import net.islbd.kothabondhu.model.pojo.RegisterInfo;
import net.islbd.kothabondhu.model.pojo.UserGmailInfo;
import net.islbd.kothabondhu.model.pojo.UserQuery;
import net.islbd.kothabondhu.model.pojo.UserStatusDetails;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.service.SinchService;
import net.islbd.kothabondhu.utility.HttpStatusCodes;
import net.islbd.kothabondhu.utility.SharedPrefUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PhoneVerifyActivity extends BaseActivity implements SinchService.StartFailedListener {
    private Splashy splashy;
    private Call<RegisterInfo> verifyCall;
    private Call<UserStatusDetails> verifyCall2;
    private SharedPreferences sharedPref;
    private Context context;
    private IApiInteractor apiInteractor;
    private String mPhoneNumber = null;
    private EditText phoneEditText;
    private TextInputLayout textInputLayout;
    private Button verifyButton;
    private ImageView splashLogo;
    private UserGmailInfo userGmailInfo;

    private static final int REQUEST_CODE_PERMISSION = 2000;

    private GoogleSignInClient mSignInClient;
    private static final int SIGN_IN = 1;
    private static final String TAG = "PhoneVerifyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);
        if(alreadyRead()) loadActivityWork();
        else loadAboutActivity();
        //verifyUser(null);
        //afterReadComplete();
    }

    private void signInWork(){
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .build();
        mSignInClient = GoogleSignIn.getClient(this, options);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = mSignInClient.getSignInIntent();
                startActivityForResult(intent, SIGN_IN);
                /*String phone = phoneEditText.getText().toString().trim();
                if (phone.isEmpty()) {
                    Toast.makeText(context, "Please insert phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                //verifyUser(phone);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                // Sign in succeeded, proceed with account
                Toast.makeText(this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                restartActivity();
                //GoogleSignInAccount acct = task.getResult();
                //System.out.println("helo");
            } else {
                Toast.makeText(this, "Sign in failed!", Toast.LENGTH_SHORT).show();
                // Sign in failed, handle failure and update UI
                // ...
            }
        }
    }

    private void restartActivity(){
        Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public boolean alreadyRead(){
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        //SharedPreferences.Editor sharedPreferencesEdit = sharedPreferences.edit();
        boolean alreadyRead = sharedPreferences.getBoolean("Read", false);
        return alreadyRead;
    }

    private void loadAboutActivity(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 2000);
    }

    private void loadActivityWork(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        context = this;

        phoneEditText = findViewById(R.id.phone_verify_EditText);
        textInputLayout = findViewById(R.id.input_layout_user);
        verifyButton = findViewById(R.id.verify_login_button);
        splashLogo = findViewById(R.id.splashLogo);

        if (!hasPermission(context)) {
            ActivityCompat.requestPermissions(PhoneVerifyActivity.this,
                    new String[]{
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    REQUEST_CODE_PERMISSION);
        } else {
            initializeData();
        }

        signInWork();
    }

    private void initializeData() {
        showVerifyButton(false);
        splashy = new Splashy(this);
        //initSplash();
        AppPresenter appPresenter = new AppPresenter();
        apiInteractor = appPresenter.getApiInterface();
        sharedPref = appPresenter.getSharedPrefInterface(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String phone = getPhoneNumber();
                verifyUser(phone);
            }
        }, 1000);
    }

    private void verifyUser(String phone) {
        userGmailInfo = getUserInfoFromGMail();
        if(userGmailInfo == null){
            Toast.makeText(this, "Please log in", Toast.LENGTH_SHORT).show();
            showVerifyButton(true);
            return;
        }
        Log.d(TAG, "verifyUser: " + userGmailInfo.toString());
        //final UserQuery userQuery = new UserQuery();
        //userQuery.setEndUserId(phone);
        verifyCall = apiInteractor.getUserAccountInfoGMail(userGmailInfo);
        verifyCall.enqueue(new Callback<RegisterInfo>() {
            @Override
            public void onResponse(Call<RegisterInfo> call, Response<RegisterInfo> response) {
                if(response.code() == HttpStatusCodes.OK){
                    RegisterInfo registerInfo = response.body();
                    if(registerInfo.getStatusCode() != null){
                        sharedPref.edit().putInt(SharedPrefUtils._STATUS_CODE, Integer.parseInt(registerInfo.getStatusCode())).apply();
                        if (!getSinchServiceInterface().isStarted()) {
                            getSinchServiceInterface().startClient(userGmailInfo.getId());
                        } else {
                            logIntoHomeScreen();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterInfo> call, Throwable t) {
                Toast.makeText(context, "Some problem occurs !", Toast.LENGTH_SHORT).show();
            }
        });
        /*verifyCall2 = apiInteractor.getUserStatus(userQuery);
        verifyCall2.enqueue(new Callback<UserStatusDetails>() {
            @Override
            public void onResponse(Call<UserStatusDetails> call, Response<UserStatusDetails> response) {
                if (response.code() == HttpStatusCodes.OK) {
                    UserStatusDetails userStatusDetails = response.body();
                    //if (userStatusDetails.getEndUserRegId() != null && userStatusDetails.getEndUserId() != null)
                    if (userStatusDetails.getEndUserId() != null) {
                        sharedPref.edit().putInt(SharedPrefUtils._USER_PHONE, Integer.parseInt(userStatusDetails.getEndUserId())).apply();
                        if (!getSinchServiceInterface().isStarted()) {
                            getSinchServiceInterface().startClient(userStatusDetails.getEndUserId());
                        } else {
                            logIntoHomeScreen();
                        }
                    } else {
                        Toast.makeText(context, "Wrong number!", Toast.LENGTH_SHORT).show();
                        showVerifyButton(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserStatusDetails> call, Throwable t) {
                Toast.makeText(PhoneVerifyActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private void logIntoHomeScreen() {
        Intent intent = new Intent(PhoneVerifyActivity.this, DocumentActivity.class);
        startActivity(intent);
        finish();
    }

    private void openAgentLogInScreen() {
        Intent intent = new Intent(PhoneVerifyActivity.this, DocumentActivity.class);
        startActivity(intent);
        finish();
    }

    private void showVerifyButton(boolean isShow) {
        if (isShow) {
            verifyButton.setVisibility(View.VISIBLE);
            phoneEditText.setVisibility(View.GONE);
            textInputLayout.setVisibility(View.GONE);
            splashLogo.setVisibility(View.VISIBLE);
        } else {
            verifyButton.setVisibility(View.GONE);
            phoneEditText.setVisibility(View.GONE);
            textInputLayout.setVisibility(View.GONE);
            splashLogo.setVisibility(View.VISIBLE);
        }
    }

    private void initSplash() {
        splashy.setInfiniteDuration(true)
                .setLogo(R.drawable.ic_b)
                .setLogoWHinDp(128, 128)
                .showTitle(false)
                .setProgressColor(R.color.white)
                .setAnimation(Splashy.Animation.GLOW_LOGO, 5000)
                .setBackgroundResource(R.drawable.splash_background)
                .setLogoScaleType(ImageView.ScaleType.FIT_CENTER)
                .setFullScreen(true)
                .showProgress(true)
                .show();
    }

    private String getPhoneNumber() {
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        mPhoneNumber = "";
        /*if (tMgr != null) {
            /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(PhoneVerifyActivity.this,
                        new String[]{
                                Manifest.permission.READ_SMS,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.READ_PHONE_NUMBERS,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        REQUEST_CODE_PERMISSION);
                return mPhoneNumber;
            }
            mPhoneNumber = tMgr.getLine1Number();
        }*/

        if (mPhoneNumber == null) mPhoneNumber = "";

        //if (mPhoneNumber.isEmpty() && BuildConfig.DEBUG) mPhoneNumber = "1863290261";

        if (mPhoneNumber.isEmpty()) mPhoneNumber = "1863290261";

        if (mPhoneNumber.isEmpty()) {
            showVerifyButton(true);
        } else {
            showVerifyButton(false);
        }
        return mPhoneNumber;
    }


    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(PhoneVerifyActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStarted() {
        logIntoHomeScreen();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (hasPermission(context)) {
                initializeData();
            } else {
                finish();
            }
        }
    }
}
