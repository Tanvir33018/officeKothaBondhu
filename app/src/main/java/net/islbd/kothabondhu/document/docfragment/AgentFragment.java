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

public class AgentFragment extends Fragment {

    private Button buttonBackAgent;
    private Button buttonUnderstandAgent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.document_agent_fragment_list_item, container, false);
        init(view);
        eventListeners();
        return view;
    }
    private void init(View view){
        buttonBackAgent = view.findViewById(R.id.buttonBackAgent);
        buttonUnderstandAgent = view.findViewById(R.id.buttonUnderstandAgent);
    }
    private void eventListeners(){
        buttonBackAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadQuestionFragment();
            }
        });
        buttonUnderstandAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPackageFragment();
            }
        });
    }
    private void loadQuestionFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_in_un, R.anim.fragment_out_un)
                .replace(R.id.fragmentContainerDocument, new QuestionFragment())
                .commit();
    }
    private void loadPackageFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .replace(R.id.fragmentContainerDocument, new PackageFragment())
                .commit();
    }
}
