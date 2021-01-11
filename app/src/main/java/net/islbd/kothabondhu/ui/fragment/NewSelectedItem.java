package net.islbd.kothabondhu.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private Button imageButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newselecteditem,container,false);

        titleName = view.findViewById(R.id.newMessageTitle);
        catagoryName = view.findViewById(R.id.newMessageCatagory);
        contentName = view.findViewById(R.id.newMessageBody);



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




}
