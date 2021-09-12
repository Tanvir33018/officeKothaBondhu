package net.islbd.kothabondhu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.model.pojo.BuyPack;
import net.islbd.kothabondhu.model.pojo.PackageInfo;
import net.islbd.kothabondhu.model.pojo.PackageStatusInfo;
import net.islbd.kothabondhu.model.pojo.UserGmailInfo;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.utility.GlobalConstants;
import net.islbd.kothabondhu.utility.HttpStatusCodes;
import net.islbd.kothabondhu.utility.SharedPrefUtils;

public class PaymentMethodActivity extends AppCompatActivity {
   // private ImageView bkashImageView, rocketImageView;
    private String packageID, packageIdentifier, packageMedia, packageDuration, packageDetails;
    private Call<PackageInfo> packageInfoCall;
    private Call<PackageStatusInfo> buyPackCall;
    private IApiInteractor apiInteractor;
    private SharedPreferences sharedPref;
    private Context context;
    private UserGmailInfo userGmailInfo;
    private BuyPack buyPack;
    private TextView paySuccessText;
    private Button goHomeButton;
    private boolean payStatus;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method_package);
        payStatus = getIntent().getBooleanExtra("abc", false);
        initializeWidgets();
        initializeData();
        if(payStatus)purchasePackage();
        eventListeners();
    }

    private UserGmailInfo getUserInfoFromGMail(){
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this); //It will return null on sign out condition
        if(googleSignInAccount != null){
            String name = googleSignInAccount.getDisplayName();
            String email = googleSignInAccount.getEmail();
            String id = googleSignInAccount.getId();
            return new UserGmailInfo(name, email, id);
            //Log.d(TAG, "onCreate: " + email);
        }
        return null;
    }

    private void initializeWidgets() {
        /*bkashImageView = findViewById(R.id.payment_bkash_imageView);
        rocketImageView = findViewById(R.id.payment_rocket_imageView);*/
        imageView = findViewById(R.id.payment_imageView);
        paySuccessText = findViewById(R.id.payment_success_textview);
        goHomeButton = findViewById(R.id.payment_success_button);

    }

    private void initializeData() {
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Payment");
        context = this;
        userGmailInfo = getUserInfoFromGMail();
        packageID = getIntent().getStringExtra(GlobalConstants.EXT_TAG_PACKAGE_ID);
        //packageIdentifier = getIntent().getStringExtra(GlobalConstants.EXT_TAG_PACKAGE_IDENTIFIER);
        packageIdentifier = userGmailInfo.getId();
        packageMedia = getIntent().getStringExtra(GlobalConstants.EXT_TAG_PACKAGE_MEDIA);
        packageDuration = getIntent().getStringExtra(GlobalConstants.EXT_TAG_PACKAGE_DURATION);
        packageDetails = getIntent().getStringExtra(GlobalConstants.EXT_TAG_PACKAGE_DETAILS);
        Toast.makeText(context, packageDetails, Toast.LENGTH_SHORT).show();
        AppPresenter appPresenter = new AppPresenter();
        apiInteractor = appPresenter.getApiInterface();
        sharedPref = appPresenter.getSharedPrefInterface(this);


        if(payStatus){
            imageView.setImageResource(R.drawable.right_sign);
            paySuccessText.setText("স্বাগতম, আপনার প্যাকেজ ক্রয় সম্পূর্ণ হয়েছে");

        }

    }

    private void eventListeners() {

        goHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("TAG", "initializeData: ");
                    Intent intent = new Intent(PaymentMethodActivity.this, HomeTabActivity.class);
                    startActivity(intent);
                    finish();
            }
        });
    }

    private void purchasePackage() {

        buyPack = new BuyPack();
        buyPack.setEndUserRegId(packageIdentifier);
        buyPack.setPackBuyMedia(packageMedia);
        buyPack.setPackId(packageID);
        buyPack.setPackBuyMobileNo("8804128");
        buyPackCall = apiInteractor.setBuyPack(buyPack);
        buyPackCall.enqueue(new Callback<PackageStatusInfo>() {
            @Override
            public void onResponse(Call<PackageStatusInfo> call, Response<PackageStatusInfo> response) {
                if (response.code() == HttpStatusCodes.OK) {
                    sharedPref.edit().putString(SharedPrefUtils._PACKAGE_MEDIA, packageMedia).apply();
                    sharedPref.edit().putString(SharedPrefUtils._PACKAGE_DETAILS, packageDetails).apply();
                    sharedPref.edit().putString(SharedPrefUtils._PACKAGE_ID, packageID).apply();
                    sharedPref.edit().putString(SharedPrefUtils._PACKAGE_IDENTIFIER, packageIdentifier).apply();
                    sharedPref.edit().putString(SharedPrefUtils._PACKAGE_DURATION, packageDuration).apply();
                    //Toast.makeText(context, "PURCHASED PACKAGE : " + packageDetails, Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(PaymentMethodActivity.this, HomeTabActivity.class);
                    startActivity(intent);
                    finish();*/
                } else {
                    Toast.makeText(context, "Something went wrong! Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PackageStatusInfo> call, Throwable t) {
                Toast.makeText(context, "Something went wrong! Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
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
    public void onBackPressed() {

    }
}
