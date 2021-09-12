package net.islbd.kothabondhu.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.ActionBar;

import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rbddevs.splashy.Splashy;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushTokenRegistrationCallback;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.UserController;
import com.sinch.android.rtc.UserRegistrationCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.document.DocumentActivity;
import net.islbd.kothabondhu.model.pojo.RegisterInfo;
import net.islbd.kothabondhu.model.pojo.UserGmailInfo;
import net.islbd.kothabondhu.model.pojo.UserStatusDetails;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.service.SinchService;
import net.islbd.kothabondhu.utility.HttpStatusCodes;
import net.islbd.kothabondhu.utility.SharedPrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Arrays;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;
import static net.islbd.kothabondhu.service.SinchService.APPLICATION_HOST;
import static net.islbd.kothabondhu.service.SinchService.APPLICATION_KEY;
import static net.islbd.kothabondhu.service.SinchService.APPLICATION_SECRET;


public class PhoneVerifyActivity extends BaseActivity implements
        SinchService.StartFailedListener,
        PushTokenRegistrationCallback,
        UserRegistrationCallback {
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
    private Button log_in_gmail;
    private LoginButton facebookButton;
    private TextView orTextView;
    private String mUserId;
    private long mSigningSequence = 1;
    private boolean tokenRegistred;
    private AppUpdateManager mUpdateManager;
    private static final Integer DAYS_FOR_FLEXIBLE_UPDATE = 5;
    private static final int MY_REQUEST_CODE = 101;

    private static final int REQUEST_CODE_PERMISSION = 2000;
    int i = 0;

    private GoogleSignInClient mSignInClient;
    private static final int SIGN_IN = 1;
    private static final String TAG = "PhoneVerifyActivity";
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private AccessToken mToken;
    private String logInFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);
        log_in_gmail = findViewById(R.id.verify_login_button);
        facebookButton = findViewById(R.id.login_button);
        orTextView = findViewById(R.id.orTextView);
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        logInFlag = "null";
        checkForUpdate();
    }

    private void checkForUpdate() {
        mUpdateManager = AppUpdateManagerFactory.create(this);
        //Toast.makeText(this, "Checking for update...", Toast.LENGTH_SHORT).show();
        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = mUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
           // Toast.makeText(this, "Checking for update 2...", Toast.LENGTH_SHORT).show();
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                try {
                  //  Toast.makeText(this, "Checking for update 3...", Toast.LENGTH_SHORT).show();
                    mUpdateManager.startUpdateFlowForResult(appUpdateInfo, IMMEDIATE,
                            this, MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else {
                if (alreadyRead())
                    loadActivityWork();
                else
                    loadAboutActivity();
            }
        });
    }


    private void signInWork() {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .build();
        mSignInClient = GoogleSignIn.getClient(this, options);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInFlag = SharedPrefUtils.LOG_IN_FLAG_GMAIL;
                Intent intent = mSignInClient.getSignInIntent();
                startActivityForResult(intent, SIGN_IN);
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(PhoneVerifyActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        logInFlag = SharedPrefUtils.LOG_IN_FLAG_FACEBOOK;
                        Toast.makeText(context, "Facebook  Login Success!", Toast.LENGTH_SHORT).show();
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(context, "Facebook Login Cancel!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException error) {

                        Toast.makeText(context, "Facebook Login Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(PhoneVerifyActivity.this, "Authentication Success",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(user);
                           // restartActivity();
                            GraphRequest mRequest = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    Log.d("Login Success", "onCompleted: ");
                                    if(object != null){
                                        try {
                                           String mEmail = object.getString("email");
                                            Log.d("Email", "onCompleted: "+mEmail);
                                           String mId = object.getString("id");
                                            Log.d("mId", "onCompleted: "+mId);
                                           //String mName = object.getString("first_name") + " " + object.getString("last_name");
                                            /*sharedPref.edit().putString(SharedPrefUtils._FACEBOOK_NAME, mName).apply();
                                            sharedPref.edit().putString(SharedPrefUtils._FACEBOOK_EMAIL, mName).apply();
                                            sharedPref.edit().putString(SharedPrefUtils._FACEBOOK_NAME, mName).apply();*/
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(PhoneVerifyActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
    }

    private UserGmailInfo getUserInfo() {
        //if(logInFlag.equals(SharedPrefUtils.LOG_IN_FLAG_GMAIL)){
            GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this); //It will return null on sign out condition
            if (googleSignInAccount != null) {
                String name = googleSignInAccount.getDisplayName();
                String email = googleSignInAccount.getEmail();
                String id = googleSignInAccount.getId();
                return new UserGmailInfo(name, email, id);
            }

        /*if(logInFlag.equals(SharedPrefUtils.LOG_IN_FLAG_FACEBOOK)){

        }*/
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Log.d("TAG", "Update flow failed! Result code: " + resultCode);
                finishAndRemoveTask();
            }
        }
        if (requestCode == SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                //Toast.makeText(this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                restartActivity();

            } else {
                Toast.makeText(this, "Sign in failed!", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void restartActivity() {
        Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public boolean alreadyRead() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        boolean alreadyRead = sharedPreferences.getBoolean("Read", false);
        return alreadyRead;
    }

    //---- Call from onCreate
    private void loadAboutActivity() {
        ActionBar actionBar = getSupportActionBar();
        /*if (actionBar != null) {
            actionBar.hide();
        }*/
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 2000);
    }


    //---- call from onCreate
    private void loadActivityWork() {
        ActionBar actionBar = getSupportActionBar();
        /*if (actionBar != null) {
            actionBar.hide();
        }*/
        context = PhoneVerifyActivity.this;

        phoneEditText = findViewById(R.id.phone_verify_EditText);
        textInputLayout = findViewById(R.id.input_layout_user);
        verifyButton = findViewById(R.id.verify_login_button);
        splashLogo = findViewById(R.id.splashLogo);

        if (!hasPermission(context)) {
            ActivityCompat.requestPermissions(PhoneVerifyActivity.this,
                    new String[]{
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_PHONE_STATE
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
        tokenRegistred = sharedPref.getBoolean("TokenRegistred", false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                 String phone = getPhoneNumber();
                verifyUser(phone);
            }
        }, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUpdateManager.getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                try {
                                    mUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            IMMEDIATE,
                                            this,
                                            MY_REQUEST_CODE);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

    }

    private void verifyUser(String phone) {
        userGmailInfo = getUserInfo();
        if (userGmailInfo == null) {
            showVerifyButton(true);
            return;
        }
        mUserId = userGmailInfo.getId();
        Log.d(TAG, "verifyUser: " + userGmailInfo.toString());
        verifyCall = apiInteractor.getUserAccountInfoGMail(userGmailInfo);
        verifyCall.enqueue(new Callback<RegisterInfo>() {
            @Override
            public void onResponse(Call<RegisterInfo> call, Response<RegisterInfo> response) {
                if (response.code() == HttpStatusCodes.OK) {
                    RegisterInfo registerInfo = response.body();
                    if (registerInfo.getStatusCode() != null) {
                        sharedPref.edit().putInt(SharedPrefUtils._STATUS_CODE, Integer.parseInt(registerInfo.getStatusCode())).apply();
                        sharedPref.edit().putString(SharedPrefUtils.USER_ID, userGmailInfo.getId()).apply();

                        if (tokenRegistred) {
                            if (!getSinchServiceInterface().isStarted()) {
                                getSinchServiceInterface().setUsername(userGmailInfo.getId());
                                getSinchServiceInterface().startClient();


                            } else {
                                logIntoHomeScreen();
                            }

                        } else {
                            UserController uc = Sinch.getUserControllerBuilder()
                                    .context(getApplicationContext())
                                    .applicationKey(APPLICATION_KEY)
                                    .userId(mUserId)
                                    .environmentHost(APPLICATION_HOST)
                                    .build();
                            uc.registerUser(PhoneVerifyActivity.this, PhoneVerifyActivity.this);

                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterInfo> call, Throwable t) {
                Toast.makeText(context, "Some problem occurs !", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void logIntoHomeScreen() {

        Intent intent = new Intent(PhoneVerifyActivity.this, DocumentActivity.class);
        startActivity(intent);
        finish();
    }


    private void showVerifyButton(boolean isShow) {
        if (isShow) {
            verifyButton.setVisibility(View.VISIBLE);
            //facebookButton.setVisibility(View.VISIBLE);
            //orTextView.setVisibility(View.VISIBLE);
            phoneEditText.setVisibility(View.GONE);
            textInputLayout.setVisibility(View.GONE);
            splashLogo.setVisibility(View.VISIBLE);
        } else {
            verifyButton.setVisibility(View.GONE);
            facebookButton.setVisibility(View.GONE);
            orTextView.setVisibility(View.GONE);
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

        if (mPhoneNumber == null) mPhoneNumber = "";

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
        Toast.makeText(PhoneVerifyActivity.this, "Something went wrong PVA", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStarted() {
        //Toast.makeText(context, "Started PVA", Toast.LENGTH_SHORT).show();
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
    @Override
    public void tokenRegistered() {
        sharedPref.edit().putBoolean("TokenRegistred", true).apply();
        //Toast.makeText(context, "Token Registered!", Toast.LENGTH_SHORT).show();
        getSinchServiceInterface().setUsername(mUserId);
        getSinchServiceInterface().startClient();
    }
    @Override
    public void tokenRegistrationFailed(SinchError sinchError) {
        Toast.makeText(context, "Token Reistration Failed PVA", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onCredentialsRequired(ClientRegistration clientRegistration) {
        String toSign = mUserId + APPLICATION_KEY + mSigningSequence + APPLICATION_SECRET;
        String signature;
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] hash = messageDigest.digest(toSign.getBytes("UTF-8"));
            signature = Base64.encodeToString(hash, Base64.DEFAULT).trim();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
        clientRegistration.register(signature, mSigningSequence++);
       // Toast.makeText(context, "Credentials", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUserRegistered() {
       // Toast.makeText(context, "User Registered!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUserRegistrationFailed(SinchError sinchError) {
        Toast.makeText(context, "Registration Failed!", Toast.LENGTH_SHORT).show();
    }
}
