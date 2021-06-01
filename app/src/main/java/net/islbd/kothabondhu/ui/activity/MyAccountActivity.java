package net.islbd.kothabondhu.ui.activity;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.document.DocumentActivity;
import net.islbd.kothabondhu.ui.fragment.MyAccountFragment;

import org.webrtc.EglBase;

import static net.islbd.kothabondhu.R.id.bottom_category;
import static net.islbd.kothabondhu.R.id.bottom_my_account;

public class MyAccountActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        initializeWidgets();
        initializeData();
        eventListeners();
    }

    private void initializeWidgets() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Account");
        Fragment fragment = new MyAccountFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.my_account_constraint_layout, fragment).commit();
    }

    private void initializeData() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.bottom_my_account);
    }

    private void eventListeners() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_general, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.bottom_home){
            Intent intent = new Intent(this, DocumentActivity.class);
            intent.putExtra("From MyAccountActivity", 1);
            startActivity(intent);
        }
        else if(id == R.id.bottom_dailer){
            Intent intent = new Intent(this, HomeTabActivity.class);
            //intent.putExtra("From MyAccountActivity", 1);
            startActivity(intent);
        }
        else if(id == R.id.bottom_package){
            Intent intent = new Intent(this, PackagesActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.bottom_category){
            Intent intent = new Intent(this, DocumentActivity.class);
            intent.putExtra("From MyAccountActivity", 2);
            startActivity(intent);
        }
        return true;
    }
}
