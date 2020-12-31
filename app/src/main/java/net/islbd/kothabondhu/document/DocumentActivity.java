package net.islbd.kothabondhu.document;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.document.docfragment.SelectionFragment;
import net.islbd.kothabondhu.document.docfragment.WelcomeFragment;
import net.islbd.kothabondhu.ui.activity.HomeTabActivity;

public class DocumentActivity extends AppCompatActivity {
    public static boolean[] selection = new boolean[7];
    public static String queryURL = "getContent.php?tipscat=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        int val = getIntent().getIntExtra("from_home_tab", -1);
        if(val == -1) loadWelcomeFragment();
        else loadSelectionFragment();
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
}