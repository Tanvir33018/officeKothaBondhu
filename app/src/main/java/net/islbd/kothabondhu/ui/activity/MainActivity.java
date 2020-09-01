package net.islbd.kothabondhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.islbd.kothabondhu.BuildConfig;
import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.utility.GlobalConstants;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (BuildConfig.TYPE.equals(GlobalConstants.TYPE_USER)) {
            Intent intent = new Intent(MainActivity.this, PhoneVerifyActivity.class);
            startActivity(intent);
        } else if (BuildConfig.TYPE.equals(GlobalConstants.TYPE_AGENT)) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
