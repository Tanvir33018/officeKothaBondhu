package net.islbd.kothabondhu.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.model.pojo.StatusInfo;
import net.islbd.kothabondhu.model.pojo.UserDetails;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.utility.HttpStatusCodes;
import net.islbd.kothabondhu.utility.SharedPrefUtils;

public class SettingsActivity extends AppCompatActivity {
    private EditText phoneEditText, userNameEditText, ageEditText;
    private AutoCompleteTextView genderAutoComp, locationAutoComp;
    private Button cancelButton, saveButton;
    private ImageView userImageView;
    private Call<StatusInfo> statusInfoCall;
    private IApiInteractor apiInteractor;
    private Context context;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initializeWidgets();
        initializeData();
        eventListeners();
    }

    private void initializeWidgets() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");
        userNameEditText = findViewById(R.id.settings_username_EditText);
        phoneEditText = findViewById(R.id.setting_phone_EditText);
        ageEditText = findViewById(R.id.settings_age_EditText);
        genderAutoComp = findViewById(R.id.settings_gender_AutoCompleteTextView);
        locationAutoComp = findViewById(R.id.settings_location_AutoCompleteTextView);
        cancelButton = findViewById(R.id.settings_cancel_button);
        saveButton = findViewById(R.id.settings_save_button);
        userImageView = findViewById(R.id.setting_user_image_ImageView);
    }

    private void initializeData() {
        context = this;
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice, getResources().getStringArray(R.array.gender));
        genderAutoComp.setAdapter(genderAdapter);
        ArrayAdapter<String> districtsAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice, getResources().getStringArray(R.array.bd_districts));
        locationAutoComp.setAdapter(districtsAdapter);
        AppPresenter appPresenter = new AppPresenter();
        apiInteractor = appPresenter.getApiInterface();
        sharedPref = appPresenter.getSharedPrefInterface(context);
        String phone = String.valueOf("0" + sharedPref.getInt(SharedPrefUtils._USER_PHONE, 0));
        phoneEditText.setText(phone);
    }

    private void eventListeners() {
        userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserData();
            }
        });
    }

    private void saveUserData() {
        String userName = userNameEditText.getText().toString();
        String userAge = ageEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String userLocation = locationAutoComp.getText().toString();
        String userGender = genderAutoComp.getText().toString();
        UserDetails userDetails = new UserDetails();
        userDetails.setUserName(userName);
        userDetails.setEndUserId(userAge);
        userDetails.setAge(phone);
        userDetails.setLocation(userLocation);
        userDetails.setGender(userGender);
        statusInfoCall = apiInteractor.setUserRegistration(userDetails);
        statusInfoCall.enqueue(new Callback<StatusInfo>() {
            @Override
            public void onResponse(Call<StatusInfo> call, Response<StatusInfo> response) {
                if (response.code() == HttpStatusCodes.OK) {
                    if (response.body() == null)
                        return;

                    String description = response.body().getDescrption();
                    Toast.makeText(context, description, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<StatusInfo> call, Throwable t) {

            }
        });
    }

    private void selectImage() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Select Photo Via");

        alertDialogBuilder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 0);
            }
        });

        alertDialogBuilder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            String fileNameSegments[] = picturePath.split("/");
            String fileName = fileNameSegments[fileNameSegments.length - 1];

            Bitmap myImg = BitmapFactory.decodeFile(picturePath);
            userImageView.setImageBitmap(myImg);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            myImg.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            //encodedString = Base64.encodeToString(byte_arr, 0);

        } else if (requestCode == 0 && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap myImg = (Bitmap) extras.get("data");
            userImageView.setImageBitmap(myImg);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            myImg.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            //encodedString = Base64.encodeToString(byte_arr, 0);

        }
    }
}
