package net.islbd.kothabondhu.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.model.pojo.PackageInfo;
import net.islbd.kothabondhu.model.pojo.PackageInfoQuery;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.presenter.IDbInteractor;
import net.islbd.kothabondhu.ui.adapter.PackageListAdapter;
import net.islbd.kothabondhu.utility.HttpStatusCodes;
import net.islbd.kothabondhu.utility.SharedPrefUtils;

public class PackageListFragment extends Fragment {
    private RecyclerView packageListRecyclerView;
    private Context context;
    private IDbInteractor dbInteractor;
    private IApiInteractor apiInteractor;
    private Call<List<PackageInfo>> packageListCall;
    private SharedPreferences sharedPref;
    private PackageListAdapter packageListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_package_list, container, false);
        initializeWidgets(view);
        initializeData();
        return view;
    }

    private void initializeWidgets(View view) {
        packageListRecyclerView = view.findViewById(R.id.package_list_recyclerView);
    }

    private void initializeData() {
        context = this.getContext();
        AppPresenter appPresenter = new AppPresenter();
        dbInteractor = appPresenter.getDbInterface(context);
        apiInteractor = appPresenter.getApiInterface();
        sharedPref = appPresenter.getSharedPrefInterface(context);
        createList();
    }

    private void createList() {
        packageListAdapter = new PackageListAdapter(context, dbInteractor);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        packageListRecyclerView.setLayoutManager(layoutManager);
        packageListRecyclerView.setLayoutManager(layoutManager);
        packageListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        packageListRecyclerView.setAdapter(packageListAdapter);

        downloadPackages();
    }

    private void downloadPackages() {
        PackageInfoQuery packageInfoQuery = new PackageInfoQuery();
        packageInfoQuery.setQ("19");
        packageInfoQuery.setMobilenumber(String.valueOf(sharedPref.getInt(SharedPrefUtils._USER_PHONE, 0)));
        packageInfoQuery.setMobilenumber("01888014");
        packageListCall = apiInteractor.getPackageList(packageInfoQuery);
        packageListCall.enqueue(new Callback<List<PackageInfo>>() {
            @Override
            public void onResponse(Call<List<PackageInfo>> call, Response<List<PackageInfo>> response) {
                if (response.code() == HttpStatusCodes.OK) {
                    List<PackageInfo> packageInfoList = response.body();
                    if (packageInfoList != null) {
                        packageListAdapter.setPackageList(packageInfoList);
                        packageListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PackageInfo>> call, Throwable t) {

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (context != null) {
            createList();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        createList();
    }

    @Override
    public void onResume() {
        super.onResume();
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
