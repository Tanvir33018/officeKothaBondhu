package net.islbd.kothabondhu.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.sinch.android.rtc.calling.Call;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import net.islbd.kothabondhu.MockData;
import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.model.pojo.PackageStatusInfo;
import net.islbd.kothabondhu.model.pojo.PackageStatusQuery;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.presenter.IDbInteractor;
import net.islbd.kothabondhu.service.SinchService;
import net.islbd.kothabondhu.ui.adapter.AgentListAdapter;
import net.islbd.kothabondhu.utility.GlobalConstants;
import net.islbd.kothabondhu.utility.HttpStatusCodes;
import net.islbd.kothabondhu.utility.SharedPrefUtils;

import retrofit2.Response;

public class AgentProfileActivity extends BaseActivity {
    private TextView agentNameTextView, locationTextView, ageTextView, sexTextView;
    private ImageView chatImageView, callImageView;
    private CircularImageView photoImageView;
    private ProgressBar agentPhotoProgressBar;
    private int selectedPackageIndex;
    private SharedPreferences sharedPref;
    private Context context;
    private IDbInteractor dbInteractor;
    private IApiInteractor apiInteractor;
    private retrofit2.Call<PackageStatusInfo> packageStatusInfoCall;
    private boolean isOnline = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_profile);

        initializeWidgets();
        initializeData();
        eventListeners();
    }

    private void initializeWidgets() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Agent Profile");
        agentNameTextView = findViewById(R.id.agent_profile_name_textview);
        locationTextView = findViewById(R.id.agent_profile_location_textview);
        ageTextView = findViewById(R.id.agent_profile_age_textview);
        sexTextView = findViewById(R.id.agent_profile_sex_textview);
        photoImageView = findViewById(R.id.agent_profile_photo_imageView);
        agentPhotoProgressBar = findViewById(R.id.agent_profile_photo_progress_bar);
        callImageView = findViewById(R.id.agent_profile_call_imageview);
        chatImageView = findViewById(R.id.agent_profile_chat_imageview);
    }

    private void initializeData() {
        AppPresenter appPresenter = new AppPresenter();
        context = this;
        sharedPref = appPresenter.getSharedPrefInterface(context);
        dbInteractor = appPresenter.getDbInterface(context);
        apiInteractor = appPresenter.getApiInterface();
        Intent intent = getIntent();
        String name = intent.getStringExtra(AgentListAdapter.NAME_TAG);
        String photoUrl = intent.getStringExtra(AgentListAdapter.PHOTO_URL_TAG);
        String age = intent.getStringExtra(AgentListAdapter.AGE_TAG);
        String location = intent.getStringExtra(AgentListAdapter.LOCATION_TAG);
        String status = intent.getStringExtra(AgentListAdapter.STATUS_TAG);
        String id = intent.getStringExtra(AgentListAdapter.ID_TAG);
        String displayName = "Name: " + name;
        String displayLocation = "Location: " + location;
        String displayAge = "Age: " + age;
        loadImage(photoUrl, photoImageView, agentPhotoProgressBar);

        agentNameTextView.setText(displayName);
        locationTextView.setText(displayLocation);
        ageTextView.setText(displayAge);
        isOnline = status.equals("1");
    }

    private void eventListeners() {
        callImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline) {
                    String id = getIntent().getStringExtra(AgentListAdapter.ID_TAG);
                    String name = getIntent().getStringExtra(AgentListAdapter.NAME_TAG);
                    String photoUrl = getIntent().getStringExtra(AgentListAdapter.PHOTO_URL_TAG);
                    //showPackageDialog(id, name, photoUrl);
                    onPackageSelection(id, name, photoUrl);
                }
            }
        });

        chatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void loadImage(String url, ImageView imageView, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        Picasso picasso = Picasso.get();
        //picasso.setDebugging(true);
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

    private void showPackageDialog(final String id, final String name, final String url) {
        selectedPackageIndex = -1;
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog);
        alertDialog.setTitle(getString(R.string.package_dialog_title));
        alertDialog.setSingleChoiceItems(new MockData().getPackageArray(), selectedPackageIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedPackageIndex = i;
            }
        });

        alertDialog.setPositiveButton(getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sharedPref.edit().putInt(SharedPrefUtils._TO_USER_PHONE, Integer.valueOf(id)).apply();
                dbInteractor.markCall(id, name, 0);
                /*Intent intent = new Intent(context, CallActivity.class);
                context.startActivity(intent);*/
                //Toast.makeText(context, String.valueOf(selectedPackageIndex), Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(context, SinchCallActivity.class);
                intent.putExtra(SinchCallActivity.EXTRA_RECIPIENT_ID, id);*/
                onPackageSelection(id, name, url);
            }
        });

        alertDialog.setNegativeButton(context.getString(R.string.dialog_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, context.getString(R.string.package_dialog_toast), Toast.LENGTH_SHORT).show();
            }
        });


        alertDialog.show();
    }

    private void onPackageSelection(String callId, String name, String url) {
        /*Call call = getSinchServiceInterface().callUser(callId);

        Intent intent = new Intent(context, CallOnGoingActivity.class);
        intent.putExtra(SinchService.CALL_ID, call.getCallId());
        intent.putExtra(GlobalConstants.EXT_TAG_URL, url);
        startActivity(intent);*/

        String endUserRegId = sharedPref.getString(SharedPrefUtils._PACKAGE_IDENTIFIER, "");
        final String fCallId = callId;
        final String fImageUrl = url;
        PackageStatusQuery packageStatusQuery = new PackageStatusQuery();
        packageStatusQuery.setEndUserRegId(endUserRegId);
        packageStatusInfoCall = apiInteractor.getPackageStatus(packageStatusQuery);
        packageStatusInfoCall.enqueue(new retrofit2.Callback<PackageStatusInfo>() {
            @Override
            public void onResponse(retrofit2.Call<PackageStatusInfo> rCall, Response<PackageStatusInfo> response) {
                if (response.code() == HttpStatusCodes.OK) {
                    Call call = getSinchServiceInterface().callUser(fCallId);
                    Intent intent = new Intent(AgentProfileActivity.this, CallOnGoingActivity.class);
                    intent.putExtra(SinchService.CALL_ID, call.getCallId());
                    intent.putExtra(GlobalConstants.EXT_TAG_URL, fImageUrl);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "Please subscribe to a package", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PackageStatusInfo> rCall, Throwable t) {
                Toast.makeText(context, "Please subscribe to a package", Toast.LENGTH_SHORT).show();
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
