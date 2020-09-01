package net.islbd.kothabondhu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.islbd.kothabondhu.R;

public class MenuActivity extends AppCompatActivity {
    private LinearLayout settingsLayout, beautyLayout, loveLayout, homeLayout, jokesLayout, packagesLayout, myAccountLayout;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().setTitle("Menu");
        initializeWidgets();
        initializeData();
        eventListeners();

    }

    private void initializeWidgets() {
        settingsLayout = findViewById(R.id.settings);
        beautyLayout = findViewById(R.id.beauty_tips);
        loveLayout = findViewById(R.id.love_tips);
        homeLayout = findViewById(R.id.home);
        jokesLayout = findViewById(R.id.jokes_tips);
        packagesLayout = findViewById(R.id.subscription_package);
        myAccountLayout = findViewById(R.id.my_account);
    }

    private void initializeData() {
        context = this;
    }

    private void eventListeners() {
        settingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, getString(R.string.nav_item_settings), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        beautyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, getString(R.string.nave_item_beauty_tips), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MenuActivity.this, BeautyActivity.class);
                startActivity(intent);
            }
        });

        loveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, getString(R.string.nav_item_love_tips), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MenuActivity.this, LoveActivity.class);
                startActivity(intent);
            }
        });

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        jokesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, getString(R.string.nav_item_jokes), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MenuActivity.this, JokesActivity.class);
                startActivity(intent);
            }
        });

        packagesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, getString(R.string.nav_item_packages), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MenuActivity.this, PackagesActivity.class);
                startActivity(intent);
            }
        });

        myAccountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, getString(R.string.nav_item_my_account), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MenuActivity.this, MyAccountActivity.class);
                startActivity(intent);
            }
        });
    }

}
