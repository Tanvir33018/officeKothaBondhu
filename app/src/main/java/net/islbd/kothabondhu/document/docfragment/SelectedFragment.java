package net.islbd.kothabondhu.document.docfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.document.Api.ApiClient;
import net.islbd.kothabondhu.document.Api.ApiUtilities;
import net.islbd.kothabondhu.document.Api.MyContent;
import net.islbd.kothabondhu.document.DocumentActivity;
import net.islbd.kothabondhu.document.DocumentAdapter;
import net.islbd.kothabondhu.ui.activity.HomeTabActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedFragment extends Fragment {
    private Button buttonDrutoKothaBolun;
    private Button buttonBack;
    private RecyclerView recyclerView;
    private DocumentAdapter documentAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.document_selected_fragment_list_item, container,false);
        getList();
        init(view);
        eventListeners();
        return view;
    }
    private String modifyQueryUrl(String url){
        for(int i = 0; i < DocumentActivity.selection.length; ++i){
            if(DocumentActivity.selection[i]) url += makeCid(i);
        }
        return url.substring(0, url.length() - 1);
    }
    private String makeCid(int cid){
        switch (cid){
            case 0: return "101,";
            case 1: return "102,";
            case 2: return "103,";
            case 3: return "104,";
            case 4: return "105,";
            case 5: return "106,";
            case 6: return "107,";
            default: return null;
        }
    }
    private void getList(){
        String modifiedUrl = modifyQueryUrl(DocumentActivity.queryURL);
        ApiClient apiClient = ApiUtilities.getApiClient();
        apiClient.getMyContent(modifiedUrl).enqueue(new Callback<ArrayList<MyContent>>() {
            @Override
            public void onResponse(Call<ArrayList<MyContent>> call, Response<ArrayList<MyContent>> response) {
                if(response.isSuccessful() && response.body() != null){
                    documentAdapter.setMyContentArrayList(response.body());
                }
                else displayToast("Server problem");
            }

            @Override
            public void onFailure(Call<ArrayList<MyContent>> call, Throwable t) {
                displayToast("Some problem occurs !");
            }
        });
    }
    private void displayToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void init(View view){
        buttonDrutoKothaBolun = view.findViewById(R.id.buttonDrutoKothaBolunSelectedFragment);
        buttonBack = view.findViewById(R.id.buttonBackSelectedFragment);
        recyclerView = view.findViewById(R.id.recyclerViewSelectedFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        documentAdapter = new DocumentAdapter();
        recyclerView.setAdapter(documentAdapter);
    }
    private void eventListeners(){
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSelectionFragment();
            }
        });
        buttonDrutoKothaBolun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadHomeTabActivity();
            }
        });
    }
    private void loadSelectionFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_in_un, R.anim.fragment_out_un)
                .replace(R.id.fragmentContainerDocument, new SelectionFragment())
                .commit();
    }
    private void loadHomeTabActivity(){
        Intent intent = new Intent(getActivity(), HomeTabActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
