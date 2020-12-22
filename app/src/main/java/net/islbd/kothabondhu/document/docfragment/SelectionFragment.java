package net.islbd.kothabondhu.document.docfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.islbd.kothabondhu.R;

public class SelectionFragment extends Fragment {
    private Button buttonBack;
    private Button buttonUnderstand;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.document_selection_fragment_list_item, container, false);
        init(view);
        eventListeners();
        return view;
    }
    private void init(View view){
        buttonBack = view.findViewById(R.id.buttonBackSelectionFragment);
        buttonUnderstand = view.findViewById(R.id.buttonUnderstandSelectionFragment);
    }
    private void eventListeners(){
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAccountFragment();
            }
        });
        buttonUnderstand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSelectedFragment();
            }
        });
    }
    private void loadSelectedFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .replace(R.id.fragmentContainerDocument, new SelectedFragment())
                .commit();
    }
    private void loadAccountFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_in_un, R.anim.fragment_out_un)
                .replace(R.id.fragmentContainerDocument, new AccountFragment())
                .commit();
    }
}
