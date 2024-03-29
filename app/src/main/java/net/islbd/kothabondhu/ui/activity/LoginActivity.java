package net.islbd.kothabondhu.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.model.pojo.AgentDetails;
import net.islbd.kothabondhu.model.pojo.AgentQuery;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.utility.HttpStatusCodes;
import net.islbd.kothabondhu.utility.SharedPrefUtils;


import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
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

        final String id = agentList.get(0).getAgentId();
        final String name = agentList.get(0).getAgentName();
        final String location = "Dhaka";
        final String url = agentList.get(0).getAgentProfilePic();
        final String age = agentList.get(0).getAgentAge() + " years";
        final String status = agentList.get(0).getOnlineStatus();
        sharedPref.edit().putInt(SharedPrefUtils._USER_PHONE, Integer.valueOf(id)).apply();

        Intent intent = new Intent(LoginActivity.this, AgentHomeActivity.class);
        intent.putExtra(PHOTO_URL_TAG, url);
        intent.putExtra(NAME_TAG, name);
        intent.putExtra(AGE_TAG, age);
        intent.putExtra(STATUS_TAG, status);
        intent.putExtra(LOCATION_TAG, location);
        intent.putExtra(ID_TAG, id);
        startActivity(intent);
        finish();
    }
}
