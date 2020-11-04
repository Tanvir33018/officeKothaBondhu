package net.islbd.kothabondhu.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
    private Call<UserStatusDetails> verifyCall;
    private SharedPreferences sharedPref;
    private Context context;
    private IApiInteractor apiInteractor;
    private String mPhoneNumber = null;
    private EditText phoneEditText;
    private TextInputLayout textInputLayout;
    private Button verifyButton;
    private ImageView splashLogo;

    private static final int REQUEST_CODE_PERMISSION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);
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
                            //Manifest.permission.READ_SMS,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    REQUEST_CODE_PERMISSION);
        } else {
            initializeData();
        }

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = phoneEditText.getText().toString().trim();
                if (phone.isEmpty()) {
                    Toast.makeText(context, "Please insert phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                verifyUser(phone);
            }
        });
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
        final UserQuery userQuery = new UserQuery();
        userQuery.setEndUserId(phone);
        verifyCall = apiInteractor.getUserStatus(userQuery);
        verifyCall.enqueue(new Callback<UserStatusDetails>() {
            @Override
            public void onResponse(Call<UserStatusDetails> call, Response<UserStatusDetails> response) {
                if (response.code() == HttpStatusCodes.OK) {
                    UserStatusDetails userStatusDetails = response.body();
                    if (userStatusDetails.getEndUserRegId() != null && userStatusDetails.getEndUserId() != null) {
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
        });
    }

    private void logIntoHomeScreen() {
        Intent intent = new Intent(PhoneVerifyActivity.this, HomeTabActivity.class);
        startActivity(intent);
        finish();
    }

    private void openAgentLogInScreen() {
        Intent intent = new Intent(PhoneVerifyActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showVerifyButton(boolean isShow) {
        if (isShow) {
            verifyButton.setVisibility(View.VISIBLE);
            phoneEditText.setVisibility(View.VISIBLE);
            textInputLayout.setVisibility(View.VISIBLE);
            splashLogo.setVisibility(View.GONE);
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
