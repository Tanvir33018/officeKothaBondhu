package net.islbd.kothabondhu.document;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.document.docfragment.SelectedFragment;
import net.islbd.kothabondhu.document.docfragment.SelectionFragment;
import net.islbd.kothabondhu.document.docfragment.WelcomeFragment;
import net.islbd.kothabondhu.ui.activity.HomeTabActivity;
import net.islbd.kothabondhu.ui.activity.MyAccountActivity;
import net.islbd.kothabondhu.ui.activity.PackagesActivity;
import net.islbd.kothabondhu.ui.fragment.PackageListFragment;
import net.islbd.kothabondhu.utility.MySharedPreferences;

import java.lang.reflect.Type;

import static net.islbd.kothabondhu.R.id.bottom_category;
import static net.islbd.kothabondhu.R.id.bottom_dailer;
import static net.islbd.kothabondhu.R.id.bottom_home;
import static net.islbd.kothabondhu.R.id.bottom_my_account;
import static net.islbd.kothabondhu.R.id.bottom_package;

public class DocumentActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public static boolean[] selection = new boolean[7];
    public static String queryURL = "getContent.php?tipscat=";
    public int Val;
    public SelectionFragment selectionFragment;
    public SharedPreferences mPreferences, selectionSharedPref;
    public static BottomNavigationView bottomNavigationView;
    public static boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) getWindow().getDecorView();


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


        selectionSharedPref = getSharedPreferences("SharedPref", MODE_PRIVATE);
        if(getIntent().getIntExtra("from_home_tab",-1) == 1){
            bottomNavigationView.setSelectedItemId(bottom_category);
            loadSelectionFragment();
        }
        else if(getIntent().getIntExtra("from_home_tab3",-1) == 3){
            bottomNavigationView.setSelectedItemId(bottom_package);
            loadPackageFrangment();
        }
        else if(getIntent().getIntExtra("from_home_tab_home", -2) == 2) {
            bottomNavigationView.setSelectedItemId(bottom_home);
            loadSelectedFragment();
        }else if(getIntent().getIntExtra("From MyAccountActivity", -2) == 1) {
            loadSelectedFragment();
        }else if(getIntent().getIntExtra("From MyAccountActivity", -2) == 2) {
            bottomNavigationView.setSelectedItemId(bottom_category);
            loadSelectionFragment();
        }else if(getIntent().getIntExtra("From PackageActivity", -2) == 2) {
            bottomNavigationView.setSelectedItemId(bottom_category);
            loadSelectionFragment();
        }else if(getIntent().getIntExtra("From PackageActivity", -2) == 1) {
            loadSelectedFragment();
        }
        else if(selectionSharedPref.getBoolean("Somporko",false) ||
                selectionSharedPref.getBoolean("Valobasa",false) ||
                selectionSharedPref.getBoolean("PremerSomporko",false) ||
                selectionSharedPref.getBoolean("NijerUnnoti",false) ||
                selectionSharedPref.getBoolean("vromon",false) ||
                selectionSharedPref.getBoolean("Sundorjo",false) ||
                selectionSharedPref.getBoolean("SadharonSastho",false)) {

            if(selectionSharedPref.getBoolean("Somporko",false)) selection[0] = true;
            if(selectionSharedPref.getBoolean("Valobasa",false)) selection[1] = true;
            if(selectionSharedPref.getBoolean("PremerSomporko",false)) selection[2] = true;
            if(selectionSharedPref.getBoolean("NijerUnnoti",false)) selection[3] = true;
            if(selectionSharedPref.getBoolean("vromon",false)) selection[4] = true;
            if(selectionSharedPref.getBoolean("Sundorjo",false)) selection[5] = true;
            if(selectionSharedPref.getBoolean("SadharonSastho",false)) selection[6] = true;
            loadSelectedFragment();
        }
        else{
            bottomNavigationView.setVisibility(View.INVISIBLE);
            loadWelcomeFragment();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreferences = getSharedPreferences("Selection", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        if(selection != null){
            for(int i=0; i<selection.length;i++ ){
                if(selection[i])editor.putBoolean(i+"KeY", true);
            }
            editor.apply();
        }
    }

    private void loadWelcomeFragment(){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .replace(R.id.fragmentContainerDocument, new WelcomeFragment())
                .commit();
    }
    private void loadSelectionFragment(){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .replace(R.id.fragmentContainerDocument, new SelectionFragment())
                .commit();
    }
    private void loadSelectedFragment(){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .replace(R.id.fragmentContainerDocument, new SelectedFragment())
                .commit();

    }

    private void loadPackageFrangment(){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .replace(R.id.fragmentContainerDocument, new PackageListFragment())
                .commit();
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
        }else if(id==bottom_package){
            Intent intent = new Intent(this, PackagesActivity.class);
            startActivity(intent);
            //loadPackageFrangment();
        }
        else if(id==bottom_category){
            loadSelectionFragment();
        }
        else if(id==bottom_home){
            loadSelectedFragment();
        }
        return true;
    }
}