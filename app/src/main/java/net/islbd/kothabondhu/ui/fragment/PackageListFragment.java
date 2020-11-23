package net.islbd.kothabondhu.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView textViewAll, textViewTitle;
    private Button buttonContinue;

    private static final String FORTY_TAKA = "20 min / 40 TK";
    private static final String HUNDRED_TAKA = "50 min / 100 TK";
    private static final String HUNDRED_AND_NINTY_TAKA = "100 min / 190 TK";
    private static final String THREE_HUNDRED_AND_EIGHTY_TAKA = "200 min / 380 TK";

    public interface ContinueWork{
        void loadMoveToPurchase();
    }

    private ContinueWork continueWork;

    private void eventListener(){
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continueWork.loadMoveToPurchase();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_package_list, container, false);
        initializeWidgets(view);
        initializeData();
        eventListener();
        return view;
    }

    private void initializeWidgets(View view) {
        packageListRecyclerView = view.findViewById(R.id.package_list_recyclerView);
        textViewAll = view.findViewById(R.id.textViewAllPackageList);
        textViewTitle = view.findViewById(R.id.textViewTitlePackageList);
        buttonContinue = view.findViewById(R.id.buttonContinuePackageList);
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
        packageListAdapter = new PackageListAdapter(context, dbInteractor, this);
        continueWork = (ContinueWork)packageListAdapter;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        packageListRecyclerView.setLayoutManager(layoutManager);
        packageListRecyclerView.setLayoutManager(layoutManager);
        packageListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        packageListRecyclerView.setAdapter(packageListAdapter);

        downloadPackages();
    }

    public void setPackageTextAcToAmount(String amount){
        switch (amount){
            case FORTY_TAKA:
                textViewTitle.setText(FORTY_TAKA);
                textViewAll.setText(getResources().getString(R.string.amount_40));
                break;
            case HUNDRED_TAKA:
                textViewTitle.setText(HUNDRED_TAKA);
                textViewAll.setText(getResources().getString(R.string.amount_100));
                break;
            case HUNDRED_AND_NINTY_TAKA:
                textViewTitle.setText(HUNDRED_AND_NINTY_TAKA);
                textViewAll.setText(getResources().getString(R.string.amount_190));
                break;
            case THREE_HUNDRED_AND_EIGHTY_TAKA:
                textViewTitle.setText(THREE_HUNDRED_AND_EIGHTY_TAKA);
                textViewAll.setText(getResources().getString(R.string.amount_380));
                break;
            default: break;
        }
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
