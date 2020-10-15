package net.islbd.kothabondhu.ui.activity;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.ui.fragment.PackageListFragment;

public class PackagesActivity extends AppCompatActivity {

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
}

