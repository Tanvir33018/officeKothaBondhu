package net.islbd.kothabondhu.ui.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.CaseMap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

/*import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;*/
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushTokenRegistrationCallback;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.UserController;
import com.sinch.android.rtc.UserRegistrationCallback;
import com.sinch.android.rtc.calling.Call;

import net.islbd.kothabondhu.R;
/*import net.islbd.kothabondhu.SendNotificationPack.APIService;
import net.islbd.kothabondhu.SendNotificationPack.Client;
import net.islbd.kothabondhu.SendNotificationPack.Data;
import net.islbd.kothabondhu.SendNotificationPack.MyResponse;
import net.islbd.kothabondhu.SendNotificationPack.NotificationSender;*/
import net.islbd.kothabondhu.config.SinchConfig;
import net.islbd.kothabondhu.document.DocumentActivity;
import net.islbd.kothabondhu.document.DocumentAdapter;
import net.islbd.kothabondhu.document.docfragment.SelectedFragment;
import net.islbd.kothabondhu.document.docfragment.SelectionFragment;
import net.islbd.kothabondhu.event.IPackageSelectListener;
import net.islbd.kothabondhu.model.pojo.MyDuration;
import net.islbd.kothabondhu.model.pojo.PackageStatusInfo;
import net.islbd.kothabondhu.model.pojo.PackageStatusQuery;
import net.islbd.kothabondhu.model.pojo.UserDuration;
import net.islbd.kothabondhu.model.pojo.UserGmailInfo;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.service.SinchService;
import net.islbd.kothabondhu.ui.fragment.AgentListFragment;
import net.islbd.kothabondhu.ui.fragment.PackageListFragment;
import net.islbd.kothabondhu.utility.GlobalConstants;
import net.islbd.kothabondhu.utility.HttpStatusCodes;
import net.islbd.kothabondhu.utility.SharedPrefUtils;

import java.security.MessageDigest;

import retrofit2.Callback;
import retrofit2.Response;

import static net.islbd.kothabondhu.service.SinchService.APPLICATION_HOST;
import static net.islbd.kothabondhu.service.SinchService.APPLICATION_KEY;
import static net.islbd.kothabondhu.service.SinchService.APPLICATION_SECRET;


public class HomeTabActivity extends BaseActivity implements
        IPackageSelectListener,
        NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener{
    //private HomeTabAdapter mSectionsPagerAdapter;
    //private ViewPager mViewPager;
    private Context context;
    private Toolbar toolbar;
    //private TabLayout tabLayout;
    private SharedPreferences sharedPreferences;
    private retrofit2.Call<PackageStatusInfo> packageStatusInfoCall;
    private IApiInteractor apiInteractor;
    private BottomNavigationView bottomNavigationView;
    private MyDuration myDuration;
    private UserDuration userDuration;
    private String mUserId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tab);

        initializeWidgets();
        initializeData();
        eventListeners();
    }

    private void initializeWidgets() {
       // toolbar = findViewById(R.id.toolbar);

    }

    private void initializeData() {
       // setSupportActionBar(toolbar);

        context = this;

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
*/
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //onNavigationItemSelected(navigationView.getMenu().getItem(0));

        sharedPreferences = new AppPresenter().getSharedPrefInterface(context);
        sharedPreferences.edit().putString(SharedPrefUtils._API_KEY, GlobalConstants.API_KEY).apply();

        AppPresenter appPresenter = new AppPresenter();
        apiInteractor = appPresenter.getApiInterface();


        mUserId = getUserInfoFromGMail().getId();


    }


    private void eventListeners() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_drawer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void showMenuActivity(View view) {
        Intent intent = new Intent(HomeTabActivity.this, MenuActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeCustomAnimation(this, R.anim.slide_up, R.anim.slide_down).toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onPackageSelection(String callId) {
        Call call = getSinchServiceInterface().callUser(callId);
        Intent intent = new Intent(HomeTabActivity.this, CallOnGoingActivity.class);
        intent.putExtra(SinchService.CALL_ID, call.getCallId());
        startActivity(intent);
    }

    @Override
    public void onPackageSelection(final String callId, final String imageUrl) {
        //String endUserRegId = sharedPreferences.getString(SharedPrefUtils._PACKAGE_IDENTIFIER, "");
       // String endUserRegId = getUserInfoFromGMail().getId();


        if (mUserId.isEmpty()) {
            Toast.makeText(context, "Please subscribe to a package", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeTabActivity.this, PackagesActivity.class);
            startActivity(intent);
            return;
        }

        final String fCallId = callId;
        final String fImageUrl = imageUrl;
        PackageStatusQuery packageStatusQuery = new PackageStatusQuery();
        packageStatusQuery.setEndUserRegId(mUserId);


        packageStatusInfoCall = apiInteractor.getPackageStatus(packageStatusQuery);
        packageStatusInfoCall.enqueue(new Callback<PackageStatusInfo>() {
            @Override
            public void onResponse(retrofit2.Call<PackageStatusInfo> rCall, Response<PackageStatusInfo> response) {
                if (response.code() == HttpStatusCodes.OK) {
                    //gotoCallOnGoingActivity(fCallId, fImageUrl);
                    balanceCheckingWork(fCallId, fImageUrl);
                } else {
                   // gotoPackageActivity();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PackageStatusInfo> rCall, Throwable t) {
                gotoPackageActivity();
            }
        });
    }

    private void balanceCheckingWork(String fCallId, String fImageUrl){
        String id = getUserInfoFromGMail().getId();
        Log.d("TAG", "balanceCheckingWork: "+id);
        userDuration = new UserDuration(id);
        retrofit2.Call<MyDuration> myDurationCall = apiInteractor.getMyDuration(userDuration);
        myDurationCall.enqueue(new retrofit2.Callback<MyDuration>() {
            @Override
            public void onResponse(retrofit2.Call<MyDuration> call, Response<MyDuration> response) {


                if(response.isSuccessful() && response.body() != null){
                    Log.d("TAG: home_tab", "onResponse: "+ response.isSuccessful());
                    try{
                        MyDuration myDuration = response.body();
                        Toast.makeText(getApplicationContext(), "Remaining Balance" + myDuration.getDuration(), Toast.LENGTH_SHORT).show();
                        double duration = Double.parseDouble(myDuration.getDuration());
                        if(duration <= 0.0){
                            //Toast.makeText(getApplicationContext(), "You do not have sufficient balance!", Toast.LENGTH_LONG).show();
                            gotoPackageActivity();
                        }
                        else{
                            gotoCallOnGoingActivity(fCallId, fImageUrl, Double.parseDouble(myDuration.getDuration()));
                        }
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "Server value error"+e.toString(), Toast.LENGTH_LONG).show();
                    }
                    //double duration = Double.parseDouble(myDuration.getDuration());
                }
                else {
                    Toast.makeText(getApplicationContext(), "Some problem occurs", Toast.LENGTH_SHORT).show();
                    //finish();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<MyDuration> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Balance check failed "+t, Toast.LENGTH_SHORT).show();

            }
        });
    }



    private void gotoPackageActivity(){
        Toast.makeText(context, "Please subscribe to a package", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomeTabActivity.this, PackagesActivity.class);
        startActivity(intent);
    }

    private void gotoCallOnGoingActivity(String fCallId, String fImageUrl, double duration){
        Call call = getSinchServiceInterface().callUser(fCallId);
        Intent intent = new Intent(HomeTabActivity.this, CallOnGoingActivity.class);
        intent.putExtra(SinchService.CALL_ID, call.getCallId());
        intent.putExtra(GlobalConstants.EXT_TAG_URL, fImageUrl);
        intent.putExtra(GlobalConstants.F_CALL_ID, fCallId);
        intent.putExtra(GlobalConstants.REMAINING_DURATION, duration);
        startActivity(intent);
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

    @Override
    protected void onResume() {
        bottomNavigationView.setSelectedItemId(R.id.bottom_dailer);
        super.onResume();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        int id = item.getItemId();
        Fragment fragment = null ;

        if( id == R.id.home){
            intent = new Intent(this, DocumentActivity.class);
            intent.putExtra("from_home_tab_home", 2);
        }else if(id == R.id.bottom_dailer || id == R.id.dailer){
            fragment = new AgentListFragment();
        }else if (id == R.id.bottom_home) {
            intent = new Intent(this, DocumentActivity.class);
            intent.putExtra("from_home_tab_home", 2);
           // fragment = new SelectedFragment();
        }else if (id == R.id.my_account || id == R.id.bottom_my_account) {
            intent = new Intent(this, MyAccountActivity.class);
        } else if (id == R.id.settings ) {
            intent = new Intent(this, SettingsActivity.class);
        } else if (id == R.id.subscription_package || id == R.id.bottom_package) {
            intent = new Intent(this, PackagesActivity.class);
            /*fragment = new PackageListFragment();*/
        } else if(id == R.id.about){
            intent = new Intent(this, AboutActivity.class);
        }
        else if(id == R.id.bottom_category){
            intent = new Intent(this, DocumentActivity.class);
            intent.putExtra("from_home_tab", 1);
        }

        if (intent != null) {
            startActivity(intent);
        }

        if (fragment != null) {
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (packageStatusInfoCall != null) {
            packageStatusInfoCall.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (packageStatusInfoCall != null) {
            packageStatusInfoCall.cancel();
        }
    }

}
