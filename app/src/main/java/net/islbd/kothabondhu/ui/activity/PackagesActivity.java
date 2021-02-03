package net.islbd.kothabondhu.ui.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.document.DocumentActivity;
import net.islbd.kothabondhu.ui.fragment.PackageListFragment;

import static net.islbd.kothabondhu.R.id.bottom_category;
import static net.islbd.kothabondhu.R.id.bottom_dailer;
import static net.islbd.kothabondhu.R.id.bottom_home;
import static net.islbd.kothabondhu.R.id.bottom_my_account;
import static net.islbd.kothabondhu.R.id.bottom_package;

public class PackagesActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);
        initializeWidgets();
        initializeData();
        eventListeners();
    }

    private void initializeWidgets() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Packages");
        Fragment fragment = new PackageListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.packages_constraint_layout, fragment).commit();

    }

    private void initializeData() {

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.bottom_package);


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
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == bottom_dailer){
            Intent intent = new Intent(this,HomeTabActivity.class);
            startActivity(intent);
        }else if(id==bottom_my_account){
            Intent intent = new Intent(this, MyAccountActivity.class);
            startActivity(intent);
        }/*else if(id==bottom_package){
            Intent intent = new Intent(this, PackagesActivity.class);
            startActivity(intent);
        }*/
        else if(id==bottom_category){
            Intent intent = new Intent(this, DocumentActivity.class);
            intent.putExtra("From PackageActivity", 2);
            startActivity(intent);
        }
        else if(id==bottom_home){
            Intent intent = new Intent(this, DocumentActivity.class);
            intent.putExtra("From PackageActivity", 1);
            startActivity(intent);
        }
        return true;
    }
}

