package net.islbd.kothabondhu.document.docfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.ui.activity.HomeTabActivity;

public class SelectedFragment extends Fragment {
    private Button buttonDrutoKothaBolun;
    private Button buttonBack;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.document_selected_fragment_list_item, container,false);
        init(view);
        eventListeners();
        return view;
    }
    private void init(View view){
        buttonDrutoKothaBolun = view.findViewById(R.id.buttonDrutoKothaBolunSelectedFragment);
        buttonBack = view.findViewById(R.id.buttonBackSelectedFragment);
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
