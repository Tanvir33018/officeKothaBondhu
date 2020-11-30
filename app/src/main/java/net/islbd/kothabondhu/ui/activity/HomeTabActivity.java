package net.islbd.kothabondhu.ui.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sinch.android.rtc.calling.Call;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.event.IPackageSelectListener;
import net.islbd.kothabondhu.model.pojo.PackageStatusInfo;
import net.islbd.kothabondhu.model.pojo.PackageStatusQuery;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.service.SinchService;
import net.islbd.kothabondhu.ui.fragment.AgentListFragment;
import net.islbd.kothabondhu.utility.GlobalConstants;
import net.islbd.kothabondhu.utility.HttpStatusCodes;
import net.islbd.kothabondhu.utility.SharedPrefUtils;

import retrofit2.Callback;
import retrofit2.Response;

public class HomeTabActivity extends BaseActivity implements IPackageSelectListener, NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    //private HomeTabAdapter mSectionsPagerAdapter;
    //private ViewPager mViewPager;
    private Context context;
    private Toolbar toolbar;
    //private TabLayout tabLayout;
    private SharedPreferences sharedPreferences;
    private retrofit2.Call<PackageStatusInfo> packageStatusInfoCall;
    private IApiInteractor apiInteractor;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tab);
        initializeWidgets();
        initializeData();
        eventListeners();
    }

    private void initializeWidgets() {
        toolbar = findViewById(R.id.toolbar);
        //mViewPager = findViewById(R.id.container);
        //tabLayout = findViewById(R.id.tabs);

    }

    private void initializeData() {
        setSupportActionBar(toolbar);

        context = this;
        //getSupportActionBar().setDisplayShowTitleEnabled(false);


        //mSectionsPagerAdapter = new HomeTabAdapter(getSupportFragmentManager(), context);

        //assert mViewPager != null;
        //mViewPager.setAdapter(mSectionsPagerAdapter);

        //assert tabLayout != null;
        //tabLayout.setupWithViewPager(mViewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        sharedPreferences = new AppPresenter().getSharedPrefInterface(context);
        sharedPreferences.edit().putString(SharedPrefUtils._API_KEY, GlobalConstants.API_KEY).apply();

        AppPresenter appPresenter = new AppPresenter();
        apiInteractor = appPresenter.getApiInterface();
    }

    private void eventListeners() {
        //event listeners here
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
        String endUserRegId = sharedPreferences.getString(SharedPrefUtils._PACKAGE_IDENTIFIER, "");
        if (endUserRegId.isEmpty()) {
            Toast.makeText(context, "Please subscribe to a package", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeTabActivity.this, PackagesActivity.class);
            startActivity(intent);
            return;
        }

        final String fCallId = callId;
        final String fImageUrl = imageUrl;
        PackageStatusQuery packageStatusQuery = new PackageStatusQuery();
        packageStatusQuery.setEndUserRegId(endUserRegId);
        packageStatusInfoCall = apiInteractor.getPackageStatus(packageStatusQuery);
        packageStatusInfoCall.enqueue(new Callback<PackageStatusInfo>() {
            @Override
            public void onResponse(retrofit2.Call<PackageStatusInfo> rCall, Response<PackageStatusInfo> response) {
                if (response.code() == HttpStatusCodes.OK) {
                    Call call = getSinchServiceInterface().callUser(fCallId);
                    Intent intent = new Intent(HomeTabActivity.this, CallOnGoingActivity.class);
                    intent.putExtra(SinchService.CALL_ID, call.getCallId());
                    intent.putExtra(GlobalConstants.EXT_TAG_URL, fImageUrl);
                    intent.putExtra(GlobalConstants.F_CALL_ID, fCallId);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "Please subscribe to a package", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeTabActivity.this, PackagesActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PackageStatusInfo> rCall, Throwable t) {
                Toast.makeText(context, "Please subscribe to a package", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        super.onResume();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Intent intent = null;
        if (id == R.id.home || id == R.id.bottom_home) {
            fragment = new AgentListFragment();
        } else if (id == R.id.my_account || id == R.id.bottom_my_account) {
            intent = new Intent(this, MyAccountActivity.class);
        } else if (id == R.id.settings || id == R.id.bottom_settings) {
            intent = new Intent(this, SettingsActivity.class);
        } else if (id == R.id.subscription_package || id == R.id.bottom_package) {
            intent = new Intent(this, PackagesActivity.class);
        } else if(id == R.id.about || id == R.id.bottom_about){
            intent = new Intent(this, AboutActivity.class);
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
