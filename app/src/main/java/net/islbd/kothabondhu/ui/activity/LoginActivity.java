package net.islbd.kothabondhu.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.WindowManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushTokenRegistrationCallback;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.UserController;
import com.sinch.android.rtc.UserRegistrationCallback;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.model.pojo.AgentDetails;
import net.islbd.kothabondhu.model.pojo.AgentQuery;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.service.SinchService;
import net.islbd.kothabondhu.utility.HttpStatusCodes;
import net.islbd.kothabondhu.utility.SharedPrefUtils;


import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static net.islbd.kothabondhu.service.SinchService.APPLICATION_HOST;
import static net.islbd.kothabondhu.service.SinchService.APPLICATION_KEY;
import static net.islbd.kothabondhu.service.SinchService.APPLICATION_SECRET;

public class LoginActivity extends BaseActivity implements PushTokenRegistrationCallback, UserRegistrationCallback {
    private Context context;
    private EditText phoneEditText, passwordEditText;
    private Button logInButton;
    private TextView registerTextView;
    private AppPresenter appPresenter;
    private SharedPreferences sharedPref;
    private Call<List<AgentDetails>> agentListCall;
    private List<AgentDetails> agentList;
    private IApiInteractor apiInteractor;
    private ProgressDialog progressDialog;
    public static final String PHOTO_URL_TAG = "_PHOTO";
    public static final String NAME_TAG = "_NAME";
    public static final String LOCATION_TAG = "_LOCATION";
    public static final String AGE_TAG = "_AGE";
    public static final String STATUS_TAG = "_STATUS";
    public static final String ID_TAG = "_ID";
    private long mSigningSequence = 1;
    private String mUserId, name, location, age, url, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.NoActionBar);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initializeWidgets();
        initializeData();
        eventListeners();
    }

    private void initializeWidgets() {
        phoneEditText = findViewById(R.id.user_logIn_EditText);
        passwordEditText = findViewById(R.id.password_logIn_EditText);
        logInButton = findViewById(R.id.login_login_button);
        registerTextView = findViewById(R.id.register_login_TextView);
    }

    private void initializeData() {
        context = this;
        appPresenter = new AppPresenter();
        sharedPref = appPresenter.getSharedPrefInterface(context);
        apiInteractor = appPresenter.getApiInterface();
        progressDialog = new ProgressDialog(context, R.style.progress_bar_style);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void eventListeners() {
        logInButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onClick(View view) {
                String userPhone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (userPhone == null || userPhone.isEmpty()) {
                    Toast.makeText(context, "Phone can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password == null || password.isEmpty()) {
                    Toast.makeText(context, "Password can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                verifyAgent(userPhone, password);
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void verifyAgent(@NotNull String phone, @NotNull String password) {
        progressDialog.setMessage(context.getString(R.string.progress_dialog_loading));
        progressDialog.show();
        AgentQuery agentQuery = new AgentQuery();
        agentQuery.setAgentId(Integer.valueOf(phone));
        agentQuery.setQ(password);
        agentListCall = apiInteractor.getAgentList(agentQuery);
        agentListCall.enqueue(new Callback<List<AgentDetails>>() {
            @Override
            public void onResponse(Call<List<AgentDetails>> call, Response<List<AgentDetails>> response) {
                if (response.code() == HttpStatusCodes.OK) {
                    agentList = response.body();
                    onAgentVerified(agentList);
                } else {
                    Toast.makeText(context,"Agent not verified",Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<AgentDetails>> call, Throwable t) {
                progressDialog.dismiss();
                agentListCall.cancel();
                Toast.makeText(context,"Something went wrong!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onAgentVerified(List<AgentDetails> agentList) {
        if (agentList == null) {
            Toast.makeText(context,"Agent not verified",Toast.LENGTH_SHORT).show();
            return;
        }
        if (agentList.isEmpty()) {
            Toast.makeText(context,"Agent not verified",Toast.LENGTH_SHORT).show();
            return;
        }

        mUserId = agentList.get(0).getAgentId();
        name = agentList.get(0).getAgentName();
        location = "Dhaka";
        url = agentList.get(0).getAgentProfilePic();
        age = agentList.get(0).getAgentAge() + " years";
        status = agentList.get(0).getOnlineStatus();



        UserController uc = Sinch.getUserControllerBuilder()
                .context(getApplicationContext())
                .applicationKey(APPLICATION_KEY)
                .userId(mUserId)//id
                .environmentHost(APPLICATION_HOST)
                .build();
        uc.registerUser(LoginActivity.this, LoginActivity.this);

    }



    @Override
    public void tokenRegistered() {
        sharedPref.edit().putInt(SharedPrefUtils._USER_PHONE, Integer.valueOf(mUserId)).apply();
        sharedPref.edit().putString(SharedPrefUtils.PHOTO_URL_TAG,url).apply();
        sharedPref.edit().putString(SharedPrefUtils.NAME_TAG, name).apply();
        sharedPref.edit().putString(SharedPrefUtils.AGE_TAG,age).apply();
        sharedPref.edit().putString(SharedPrefUtils.STATUS_TAG,status).apply();
        sharedPref.edit().putString(SharedPrefUtils.LOCATION_TAG,location).apply();
        sharedPref.edit().putString(SharedPrefUtils.ID_TAG,mUserId).apply();

        Toast.makeText(context, "Token Reistered!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LoginActivity.this, AgentHomeActivity.class);
        intent.putExtra(PHOTO_URL_TAG, url);
        intent.putExtra(NAME_TAG, name);
        intent.putExtra(AGE_TAG, age);
        intent.putExtra(STATUS_TAG, status);
        intent.putExtra(LOCATION_TAG, location);
        intent.putExtra(ID_TAG, mUserId);
        startActivity(intent);
        finish();

    }

    @Override
    public void tokenRegistrationFailed(SinchError sinchError) {
        Toast.makeText(context, "Token Reistration Failed AHA"+sinchError.toString(), Toast.LENGTH_SHORT).show();
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
        Toast.makeText(context, "Credentials", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUserRegistered() {
        Toast.makeText(context, "User Registered!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserRegistrationFailed(SinchError sinchError) {
        Toast.makeText(context, "User Reistration Failed AHA"+sinchError.toString(), Toast.LENGTH_SHORT).show();
    }
}
