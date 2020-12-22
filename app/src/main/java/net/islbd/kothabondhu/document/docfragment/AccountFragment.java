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

public class AccountFragment extends Fragment {
    private Button buttonBackAccount;
    private Button buttonUnderstandAccount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.document_account_fragment_list_item, container, false);
        init(view);
        eventListeners();
        return view;
    }
    private void init(View view){
        buttonBackAccount = view.findViewById(R.id.buttonBackAccount);
        buttonUnderstandAccount = view.findViewById(R.id.buttonUnderstandAccount);
    }
    private void eventListeners(){
        buttonBackAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPackageFragment();
            }
        });
        buttonUnderstandAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSelectionFragment();
            }
        });
    }
    private void loadSelectionFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .replace(R.id.fragmentContainerDocument, new SelectionFragment())
                .commit();
    }
    private void loadPackageFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_in_un, R.anim.fragment_out_un)
                .replace(R.id.fragmentContainerDocument, new PackageFragment())
                .commit();
    }
}
