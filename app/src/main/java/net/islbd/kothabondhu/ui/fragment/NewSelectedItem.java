package net.islbd.kothabondhu.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.document.Api.ApiClient;
import net.islbd.kothabondhu.document.Api.ApiUtilities;
import net.islbd.kothabondhu.document.Api.MyContent;
import net.islbd.kothabondhu.document.DocumentActivity;
import net.islbd.kothabondhu.document.DocumentAdapter;
import net.islbd.kothabondhu.document.docfragment.SelectedFragment;
import net.islbd.kothabondhu.ui.adapter.SelectedAdapter;

import java.util.ArrayList;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewSelectedItem extends Fragment {

    private TextView titleName,catagoryName;
    //private ExpandableTextView contentName;
    private TextView contentName;
    private RecyclerView recyclerView;
    private SelectedAdapter selectedAdapter;
    private Fragment fragment;
    private ImageButton imageButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newselecteditem,container,false);

        titleName = view.findViewById(R.id.newMessageTitle);
        catagoryName = view.findViewById(R.id.newMessageCatagory);
        contentName = view.findViewById(R.id.newMessageBody);

        view.setBackgroundColor(Color.parseColor("#31E6D4"));

        Bundle bundle= getArguments();
        if(bundle != null){
            titleName.setText(bundle.getString("Title"));
            catagoryName.setText(bundle.getString("Catagory"));
            contentName.setText(bundle.getString("Content"));
        }

        imageButton = view.findViewById(R.id.image_button);
        //init(view);
        //getList();
        eventClick();

        return view;
    }

    private void eventClick() {

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                        .replace(R.id.fragmentContainerDocument, new SelectedFragment())
                        .commit();
            }
        });
    }


    /*private String modifyQueryUrl(String url){
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
                    selectedAdapter.setNewContentArrayList(response.body());
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
        //if(message != null)
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void init(View view){
        *//*buttonDrutoKothaBolun = view.findViewById(R.id.buttonDrutoKothaBolunSelectedFragment);
        buttonBack = view.findViewById(R.id.buttonBackSelectedFragment);*//*
        recyclerView = view.findViewById(R.id.recyclerViewSelectedFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        selectedAdapter = new SelectedAdapter();
        recyclerView.setAdapter(selectedAdapter);
        imageButton = view.findViewById(R.id.image_button);
    }


    */

}
