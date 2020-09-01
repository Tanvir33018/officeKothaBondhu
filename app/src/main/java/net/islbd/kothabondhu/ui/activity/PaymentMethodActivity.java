package net.islbd.kothabondhu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.model.pojo.BuyPack;
import net.islbd.kothabondhu.model.pojo.PackageInfo;
import net.islbd.kothabondhu.model.pojo.PackageInfoQuery;
import net.islbd.kothabondhu.model.pojo.PackageStatusInfo;
import net.islbd.kothabondhu.model.pojo.StatusInfo;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.utility.GlobalConstants;
import net.islbd.kothabondhu.utility.HttpStatusCodes;
import net.islbd.kothabondhu.utility.SharedPrefUtils;

public class PaymentMethodActivity extends AppCompatActivity {
    private ImageView bkashImageView, rocketImageView;
    private String packageID, packageIdentifier, packageMedia, packageDuration, packageDetails;
    private Call<PackageInfo> packageInfoCall;
    private Call<PackageStatusInfo> buyPackCall;
    private IApiInteractor apiInteractor;
    private SharedPreferences sharedPref;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method_package);
        initializeWidgets();
        initializeData();
        eventListeners();
    }

    private void initializeWidgets() {
        bkashImageView = findViewById(R.id.payment_bkash_imageView);
        rocketImageView = findViewById(R.id.payment_rocket_imageView);
    }

    private void initializeData() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Payment");
        context = this;
        packageID = getIntent().getStringExtra(GlobalConstants.EXT_TAG_PACKAGE_ID);
        packageIdentifier = getIntent().getStringExtra(GlobalConstants.EXT_TAG_PACKAGE_IDENTIFIER);
        packageMedia = getIntent().getStringExtra(GlobalConstants.EXT_TAG_PACKAGE_MEDIA);
        packageDuration = getIntent().getStringExtra(GlobalConstants.EXT_TAG_PACKAGE_DURATION);
        packageDetails = getIntent().getStringExtra(GlobalConstants.EXT_TAG_PACKAGE_DETAILS);
        AppPresenter appPresenter = new AppPresenter();
        apiInteractor = appPresenter.getApiInterface();
        sharedPref = appPresenter.getSharedPrefInterface(this);
    }

    private void eventListeners() {
        bkashImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purchasePackage(GlobalConstants.PAYMENT_TAG_BKASH);
            }
        });

        rocketImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purchasePackage(GlobalConstants.PAYMENT_TAG_ROCKET);
            }
        });
    }

    private void purchasePackage(Integer gateway) {
        switch (gateway) {
            case GlobalConstants.PAYMENT_TAG_ROCKET:
                // TODO: implement gateway for rocket
                break;
            case GlobalConstants.PAYMENT_TAG_BKASH:
                // TODO: implement gateway for bkash
                break;
        }

        /*final PackageInfoQuery packageInfoQuery = new PackageInfoQuery();
        packageInfoQuery.setQ(packageMedia);
        packageInfoQuery.setMobilenumber(String.valueOf(sharedPref.getInt(SharedPrefUtils._USER_PHONE, 0)));
        packageInfoQuery.setMobilenumber("01888014");
        packageInfoCall = apiInteractor.getPackageDetails(packageInfoQuery);
        packageInfoCall.enqueue(new Callback<PackageInfo>() {
            @Override
            public void onResponse(Call<PackageInfo> call, Response<PackageInfo> response) {
                if (response.code() == HttpStatusCodes.OK) {
                    PackageInfo packageInfo = response.body();

                    if (packageInfo != null) {
                        sharedPref.edit().putString(SharedPrefUtils._PACKAGE_MEDIA, packageInfo.getMedia()).apply();
                        sharedPref.edit().putString(SharedPrefUtils._PACKAGE_DETAILS, packageInfo.getPackageDetails()).apply();
                        sharedPref.edit().putString(SharedPrefUtils._PACKAGE_ID, packageInfo.getPkgId()).apply();
                        sharedPref.edit().putString(SharedPrefUtils._PACKAGE_IDENTIFIER, packageInfo.getPkgid()).apply();
                        sharedPref.edit().putString(SharedPrefUtils._PACKAGE_DURATION, packageInfo.getPkgDuration()).apply();
                        Toast.makeText(context, "PURCHASED PACKAGE : " + packageInfo.getPackageDetails(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PackageInfo> call, Throwable t) {
                Toast.makeText(context, "Something went wrong! Please try again.", Toast.LENGTH_SHORT).show();
            }
        });*/

        final BuyPack buyPack = new BuyPack();
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
                    Toast.makeText(context, "PURCHASED PACKAGE : " + packageDetails, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PaymentMethodActivity.this, HomeTabActivity.class);
                    startActivity(intent);
                    finish();
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
}
