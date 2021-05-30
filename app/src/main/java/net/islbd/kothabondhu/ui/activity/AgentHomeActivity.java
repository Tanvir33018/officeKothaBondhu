package net.islbd.kothabondhu.ui.activity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.ActionBar;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushTokenRegistrationCallback;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.UserController;
import com.sinch.android.rtc.UserRegistrationCallback;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.config.SinchConfig;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.service.SinchService;
import net.islbd.kothabondhu.utility.SharedPrefUtils;

import java.security.MessageDigest;

import static net.islbd.kothabondhu.service.SinchService.APPLICATION_HOST;
import static net.islbd.kothabondhu.service.SinchService.APPLICATION_KEY;
import static net.islbd.kothabondhu.service.SinchService.APPLICATION_SECRET;


public class AgentHomeActivity extends BaseActivity {

    private static final int REQUEST_CODE_PERMISSION = 2000;
    private Context context;
    private TextView agentNameTextView, locationTextView, ageTextView, sexTextView;
    private CircularImageView photoImageView;
    private ProgressBar agentPhotoProgressBar;
    //private IApiInteractor apiInteractor;
    private SharedPreferences sharedPref;
    private DatabaseReference databaseReference;
    private long mSigningSequence = 1;

    private String name, photoUrl, age, location, status, id, displayName, displayLocation, displayAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_home);

        //databaseReference = FirebaseDatabase.getInstance().getReference();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        context = this;
        if (!hasPermission(context)) {
            ActivityCompat.requestPermissions(AgentHomeActivity.this,
                    new String[]{
                            Manifest.permission.READ_SMS,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    REQUEST_CODE_PERMISSION);
        }else {
            initializeWidgets();
            initializeData();
            eventListeners();
        }

    }

    /*public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }*/

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initializeWidgets() {
        agentNameTextView = findViewById(R.id.agent_profile_name_textview);
        locationTextView = findViewById(R.id.agent_profile_location_textview);
        ageTextView = findViewById(R.id.agent_profile_age_textview);
        sexTextView = findViewById(R.id.agent_profile_sex_textview);
        photoImageView = findViewById(R.id.agent_profile_photo_imageView);
        agentPhotoProgressBar = findViewById(R.id.agent_profile_photo_progress_bar);
    }

    private void initializeData() {
        AppPresenter appPresenter = new AppPresenter();
        sharedPref = appPresenter.getSharedPrefInterface(context);
        context = this;
        Intent intent = getIntent();
        Log.d("TAG", "initializeData: ");
        name = intent.getStringExtra(LoginActivity.NAME_TAG);
        photoUrl = intent.getStringExtra(LoginActivity.PHOTO_URL_TAG);
        age = intent.getStringExtra(LoginActivity.AGE_TAG);
        location = intent.getStringExtra(LoginActivity.LOCATION_TAG);
        status = intent.getStringExtra(LoginActivity.STATUS_TAG);
        id = intent.getStringExtra(LoginActivity.ID_TAG);
        //sharedPref.edit().putString(SharedPrefUtils.USER_ID,id).commit();
        displayName = "Name: " + name;
        displayLocation = "Location: " + location;
        displayAge = "Age: " + age;
        //deviceTokenPush();
        displayData();

    }

    /*private void deviceTokenPush() {
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken)
        databaseReference.child(id).setValue(token);
        Toast.makeText(context, "Token Pushed", Toast.LENGTH_SHORT).show();
    }*/

    private void displayData() {
        loadImage(photoUrl, photoImageView, agentPhotoProgressBar);
        agentNameTextView.setText(displayName);
        locationTextView.setText(displayLocation);
        ageTextView.setText(displayAge);
    }

    private void loadImage(String url, ImageView imageView, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        Picasso picasso = Picasso.get();

        Log.d("TAG", "loadImage: ");
        picasso.load(url).error(R.drawable.ic_person).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void eventListeners() {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (hasPermission(context)) {
                reload(this);
            } else {
                finish();
            }
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putString(LoginActivity.PHOTO_URL_TAG, photoUrl);
        outState.putString(LoginActivity.NAME_TAG, name);
        outState.putString(LoginActivity.AGE_TAG, age);
        outState.putString(LoginActivity.STATUS_TAG, status);
        outState.putString(LoginActivity.LOCATION_TAG, location);
        outState.putString(LoginActivity.ID_TAG, id);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        name = savedInstanceState.getString(LoginActivity.NAME_TAG);
        photoUrl = savedInstanceState.getString(LoginActivity.PHOTO_URL_TAG);
        age = savedInstanceState.getString(LoginActivity.AGE_TAG);
        location = savedInstanceState.getString(LoginActivity.LOCATION_TAG);
        status = savedInstanceState.getString(LoginActivity.STATUS_TAG);
        id = savedInstanceState.getString(LoginActivity.ID_TAG);
        displayName = "Name: " + name;
        displayLocation = "Location: " + location;
        displayAge = "Age: " + age;

        displayData();
    }



}
