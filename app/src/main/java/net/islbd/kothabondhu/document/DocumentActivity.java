package net.islbd.kothabondhu.document;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.document.docfragment.WelcomeFragment;
import net.islbd.kothabondhu.ui.activity.HomeTabActivity;

public class DocumentActivity extends AppCompatActivity {
    public static boolean[] selection = new boolean[7];
    public static String queryURL = "getContent.php?tipscat=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        loadWelcomeFragment();
    }
    private void loadWelcomeFragment(){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .replace(R.id.fragmentContainerDocument, new WelcomeFragment())
                .commit();
    }
}