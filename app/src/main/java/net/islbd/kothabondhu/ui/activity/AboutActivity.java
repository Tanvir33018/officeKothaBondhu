package net.islbd.kothabondhu.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.islbd.kothabondhu.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MR TANVIR
        setContentView(R.layout.activity_about);
        Button buttonContinue = findViewById(R.id.buttonContinueAbout);
        boolean alReadyRead = alreadyRead();
        if(alReadyRead) buttonContinue.setText(getString(R.string.back_bangla));
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!alReadyRead){
                    setSharedPref();
                    loadPhoneVerifyActivity();
                }
                else finish();
            }
        });
    }

    public boolean alreadyRead(){
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        //SharedPreferences.Editor sharedPreferencesEdit = sharedPreferences.edit();
        boolean alreadyRead = sharedPreferences.getBoolean("Read", false);
        return alreadyRead;
    }

    private void setSharedPref(){
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEdit = sharedPreferences.edit();
        sharedPreferencesEdit.putBoolean("Read", true);
        sharedPreferencesEdit.apply();
    }

    private void loadPhoneVerifyActivity(){
        Intent intent = new Intent(getApplicationContext(), PhoneVerifyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}