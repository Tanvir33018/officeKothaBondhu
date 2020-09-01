package net.islbd.kothabondhu.ui.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import net.islbd.kothabondhu.event.IPackageSelectListener;
import net.islbd.kothabondhu.model.pojo.AgentDetails;
import net.islbd.kothabondhu.model.pojo.AgentQuery;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.presenter.IDbInteractor;
import net.islbd.kothabondhu.ui.activity.HomeTabActivity;
import net.islbd.kothabondhu.ui.adapter.AgentListAdapter;
import net.islbd.kothabondhu.utility.HttpStatusCodes;

public class AgentListFragment extends Fragment {
    private RecyclerView newsSourceRecyclerView;
    private Context context;
    private IDbInteractor dbInteractor;
    private IApiInteractor apiInteractor;
    private ProgressDialog progressDialog;
    private Call<List<AgentDetails>> agentListCall;
    private List<AgentDetails> agentList;
    private SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agent_list, container, false);
        initializeWidgets(view);
        initializeData();
        return view;
    }

    private void initializeWidgets(View view) {
        newsSourceRecyclerView = view.findViewById(R.id.source_list_recyclerView);
    }

    private void initializeData() {
        context = getActivity();
        AppPresenter appPresenter = new AppPresenter();
        dbInteractor = appPresenter.getDbInterface(context);
        apiInteractor = appPresenter.getApiInterface();
        sharedPref = appPresenter.getSharedPrefInterface(context);
        progressDialog = new ProgressDialog(context, R.style.progress_bar_style);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        downloadList();
        //createList(null);
    }

    private void createList(List<AgentDetails> agents) {
        IPackageSelectListener packageSelectListener = (HomeTabActivity) getActivity();
        AgentListAdapter agentListAdapter = new AgentListAdapter(context, packageSelectListener, dbInteractor, sharedPref);
        agentListAdapter.setAgentList(agents);
       /* StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);*/
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        newsSourceRecyclerView.setLayoutManager(layoutManager);
        newsSourceRecyclerView.setLayoutManager(layoutManager);
        newsSourceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        newsSourceRecyclerView.setAdapter(agentListAdapter);
        agentListAdapter.notifyDataSetChanged();
    }

    private void downloadList() {
        progressDialog.setMessage(context.getString(R.string.progress_dialog_loading));
        progressDialog.show();
        AgentQuery agentQuery = new AgentQuery();
        agentQuery.setAgentId(0);
        agentQuery.setQ("all");
        agentListCall = apiInteractor.getAgentList(agentQuery);
        agentListCall.enqueue(new Callback<List<AgentDetails>>() {
            @Override
            public void onResponse(Call<List<AgentDetails>> call, Response<List<AgentDetails>> response) {
                if (response.code() == HttpStatusCodes.OK) {
                    agentList = response.body();
                    createList(agentList);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<AgentDetails>> call, Throwable t) {
                progressDialog.dismiss();
                agentListCall.cancel();
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && agentList != null) {
            createList(agentList);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
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
        flush();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flush();

    }

    private void flush() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (agentListCall != null) {
            agentListCall.cancel();
        }
    }

}
