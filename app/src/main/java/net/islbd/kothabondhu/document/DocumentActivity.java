package net.islbd.kothabondhu.document;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.document.docfragment.SelectedFragment;
import net.islbd.kothabondhu.document.docfragment.SelectionFragment;
import net.islbd.kothabondhu.document.docfragment.WelcomeFragment;
import net.islbd.kothabondhu.ui.activity.HomeTabActivity;

import java.lang.reflect.Type;

public class DocumentActivity extends AppCompatActivity  {
    public static boolean[] selection = new boolean[7];
    public static String queryURL = "getContent.php?tipscat=";
    public int Val;
    public SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        if(getIntent().getIntExtra("from_home_tab",-1) == 1){
            //loadData();
            loadSelectionFragment();
        }
        else if(getIntent().getIntExtra("from_home_tab_home", -2) == 2) {
            loadSelectedFragment();
        }
        else loadWelcomeFragment();

       /* int val = getIntent().getIntExtra("from_home_tab", -1);
        if(val == 1) loadSelectionFragment();
        else loadWelcomeFragment();*/


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
    /*public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<boolean[]>() {}.getType();
        boolean[] selectS = gson.fromJson(json, type);
        *//*if (selection == null) {
            selection = selectS;
        }*//*

    }*/

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


}