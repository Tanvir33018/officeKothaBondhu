package net.islbd.kothabondhu.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.model.pojo.CallHistoryDetails;
import net.islbd.kothabondhu.model.pojo.PackageHistoryDetails;
import net.islbd.kothabondhu.model.pojo.UserAccountInfo;
import net.islbd.kothabondhu.model.pojo.UserQuery;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.ui.activity.SettingsActivity;
import net.islbd.kothabondhu.ui.adapter.CallLogListAdapter;
import net.islbd.kothabondhu.ui.adapter.PackageHistoryListAdapter;
import net.islbd.kothabondhu.utility.HttpStatusCodes;
import net.islbd.kothabondhu.utility.SharedPrefUtils;

public class MyAccountFragment extends Fragment {
    private RecyclerView callHistoryRecyclerView, packageHistoryRecyclerView;
    private ImageView editImageView;
    private Context context;
    private SharedPreferences sharedPref;
    private IApiInteractor apiInteractor;
    private CallLogListAdapter callLogListAdapter;
    private Call<List<CallHistoryDetails>> historyListCall;
    private Call<UserAccountInfo> userAccountInfoCall;
    private PackageHistoryListAdapter packageHistoryListAdapter;
    private TextView nameTextView, ageTextView, sexTextView, locationTextView,
            phoneTextView, timeLeftTextView, lastCallDurationTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        initializeWidgets(view);
        initializeData();
        eventListeners();

        return view;
    }

    private void initializeWidgets(View view) {
        callHistoryRecyclerView = view.findViewById(R.id.call_history_recyclerView);
        packageHistoryRecyclerView = view.findViewById(R.id.package_history_recyclerView);
        editImageView = view.findViewById(R.id.account_edit_imageView);
        nameTextView = view.findViewById(R.id.account_name_textView);
        nameTextView = view.findViewById(R.id.account_name_textView);
        sexTextView = view.findViewById(R.id.account_gender_textView);
        phoneTextView = view.findViewById(R.id.account_phone_textView);
        locationTextView = view.findViewById(R.id.account_location_textView);
        ageTextView = view.findViewById(R.id.account_age_textView);
        lastCallDurationTextView = view.findViewById(R.id.account_last_call_duration_textView);
        timeLeftTextView = view.findViewById(R.id.account_time_left_textView);
    }

    private void initializeData() {
        context = getActivity();
        AppPresenter appPresenter = new AppPresenter();
        sharedPref = appPresenter.getSharedPrefInterface(context);
        apiInteractor = appPresenter.getApiInterface();
        initializeHistoryList();
    }

    private void eventListeners() {
        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeHistoryList() {
        callLogListAdapter = new CallLogListAdapter();
        callHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        callHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        callHistoryRecyclerView.setAdapter(callLogListAdapter);

        packageHistoryListAdapter = new PackageHistoryListAdapter();
        packageHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        packageHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        packageHistoryRecyclerView.setAdapter(packageHistoryListAdapter);
    }


    /*private void downLoadCallHistory() {
        CallHistoryQuery callHistoryQuery = new CallHistoryQuery();
        callHistoryQuery.setUserid("1");
        callHistoryQuery.setUserMobileno("01863290261");
        historyListCall = apiInteractor.getCallHistoryList(callHistoryQuery);
        historyListCall.enqueue(new Callback<List<CallHistoryDetails>>() {
            @Override
            public void onResponse(Call<List<CallHistoryDetails>> call, Response<List<CallHistoryDetails>> response) {
                if (response.code() == HttpStatusCodes.OK) {
                    if (response.body() == null)
                        return;

                    callLogListAdapter.setCallHistoryDetailsList(response.body());
                    callLogListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<CallHistoryDetails>> call, Throwable t) {

            }
        });
    }*/

    private void downLoadUserAccountInfo() {
        String phone = "0" + String.valueOf(sharedPref.getInt(SharedPrefUtils._USER_PHONE, 0));
        UserQuery userQuery = new UserQuery();
        userQuery.setEndUserId(phone);
        userAccountInfoCall = apiInteractor.getUserAccountInfo(userQuery);
        userAccountInfoCall.enqueue(new Callback<UserAccountInfo>() {
            @Override
            public void onResponse(Call<UserAccountInfo> call, Response<UserAccountInfo> response) {
                if (response.code() == HttpStatusCodes.OK) {
                    if (response.body() == null)
                        return;

                    UserAccountInfo userAccountInfo = response.body();
                    String userName = userAccountInfo.getUserInfo().getName();
                    String userGender = "Sex : " + userAccountInfo.getUserInfo().getUsergender();
                    String userAge = "Age : " + userAccountInfo.getUserInfo().getUserAge();
                    String userLocation = "From : " + userAccountInfo.getUserInfo().getUserLocation();
                    String userPhone = "Phone : " + userAccountInfo.getUserInfo().getEndUserId();
                    String activationDate = userAccountInfo.getUserInfo().getActiveDate();
                    String timeLeft = "Time Left : ";
                    String lastCallDuration = "Last Call Duration : ";
                    List<CallHistoryDetails> callHistoryDetailsList = userAccountInfo.getCallHistory();
                    List<PackageHistoryDetails> packageHistoryDetailsList = userAccountInfo.getPackageHistoryDetailsList();

                    nameTextView.setText(userName);
                    ageTextView.setText(userAge);
                    sexTextView.setText(userGender);
                    phoneTextView.setText(userPhone);
                    locationTextView.setText(userLocation);
                    timeLeftTextView.setText(timeLeft);
                    lastCallDurationTextView.setText(lastCallDuration);


                    callLogListAdapter.setCallHistoryDetailsList(callHistoryDetailsList);
                    callLogListAdapter.notifyDataSetChanged();

                    packageHistoryListAdapter.setPackageHistoryDetailsList(packageHistoryDetailsList);
                    packageHistoryListAdapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(context, "ERROR : " + response.code() + "\n" + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserAccountInfo> call, Throwable t) {

            }
        });
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (context != null) {
            downLoadUserAccountInfo();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        downLoadUserAccountInfo();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
