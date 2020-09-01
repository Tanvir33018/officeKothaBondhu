package net.islbd.kothabondhu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

import net.islbd.kothabondhu.R;

public class RegistrationActivity extends AppCompatActivity {
    private Context context;
    private TextView logInTextView;
    private EditText phoneEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initializeWidgets();
        initializeData();
        eventListeners();
    }

    private void initializeWidgets() {
        logInTextView = findViewById(R.id.login_register_TextView);
        phoneEditText = findViewById(R.id.user_register_EditText);
        passwordEditText = findViewById(R.id.password_register_EditText);
        confirmPasswordEditText = findViewById(R.id.confirm_password_register_EditText);
        registerButton = findViewById(R.id.register_register_button);
    }

    private void initializeData() {
        context = this;
    }

    private void eventListeners() {
        logInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
