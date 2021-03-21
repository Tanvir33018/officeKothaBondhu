package net.islbd.kothabondhu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import net.islbd.kothabondhu.BuildConfig;
import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.utility.GlobalConstants;
import net.islbd.kothabondhu.utility.SharedPrefUtils;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private AppPresenter appPresenter;
    private Context context = this;

    public static final String PHOTO_URL_TAG = "_PHOTO";
    public static final String NAME_TAG = "_NAME";
    public static final String LOCATION_TAG = "_LOCATION";
    public static final String AGE_TAG = "_AGE";
    public static final String STATUS_TAG = "_STATUS";
    public static final String ID_TAG = "_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appPresenter = new AppPresenter();
        preferences = appPresenter.getSharedPrefInterface(context);
        int ID = preferences.getInt(SharedPrefUtils._USER_PHONE,1007);
        if (BuildConfig.TYPE.equals(GlobalConstants.TYPE_USER)) {
            Intent intent = new Intent(MainActivity.this, PhoneVerifyActivity.class);
            startActivity(intent);
        } else if (BuildConfig.TYPE.equals(GlobalConstants.TYPE_AGENT)) {
            if(ID == 1007){
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(this, AgentHomeActivity.class);
                intent.putExtra(PHOTO_URL_TAG, preferences.getString(SharedPrefUtils.PHOTO_URL_TAG,""));
                intent.putExtra(NAME_TAG, preferences.getString(SharedPrefUtils.NAME_TAG,""));
                intent.putExtra(AGE_TAG, preferences.getString(SharedPrefUtils.AGE_TAG,""));
                intent.putExtra(STATUS_TAG, preferences.getString(SharedPrefUtils.STATUS_TAG,""));
                intent.putExtra(LOCATION_TAG, preferences.getString(SharedPrefUtils.LOCATION_TAG,""));
                intent.putExtra(ID_TAG, preferences.getString(SharedPrefUtils.ID_TAG,""));
                startActivity(intent);
            }

        }
        finish();
    }
}
