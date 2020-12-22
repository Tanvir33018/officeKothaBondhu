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

public class PackageFragment extends Fragment {
    private Button buttonBackPackage;
    private Button buttonUnderstandPackage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.document_package_fragment_list_item, container, false);
        init(view);
        eventListeners();
        return view;
    }
    private void init(View view){
        buttonBackPackage = view.findViewById(R.id.buttonBackPackage);
        buttonUnderstandPackage = view.findViewById(R.id.buttonUnderstandPackage);
    }
    private void eventListeners(){
        buttonUnderstandPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAccountFragment();
            }
        });
        buttonBackPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAgentFragment();
            }
        });
    }
    private void loadAccountFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .replace(R.id.fragmentContainerDocument, new AccountFragment())
                .commit();
    }
    private void loadAgentFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_in_un, R.anim.fragment_out_un)
                .replace(R.id.fragmentContainerDocument, new AgentFragment())
                .commit();
    }
}
