package net.islbd.kothabondhu.document.docfragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.document.DocumentActivity;
import net.islbd.kothabondhu.presenter.AppPresenter;

import java.util.Arrays;

public class SelectionFragment extends Fragment {
    private Button buttonBack;
    private Button buttonUnderstand;
    private CheckBox somporko, valobasha, premersomoporko, nijerunnoti, vromon, sundorjotok, sadharonsastho;

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
        somporko = view.findViewById(R.id.checkBoxSomporko);
        valobasha = view.findViewById(R.id.checkboxValobasharKotha);
        premersomoporko = view.findViewById(R.id.checkboxPremerSomporko);
        nijerunnoti = view.findViewById(R.id.checkBoxNijerUnnoti);
        vromon = view.findViewById(R.id.checkBoxVromon);
        sundorjotok = view.findViewById(R.id.checkboxSundorjoTok);
        sadharonsastho = view.findViewById(R.id.checkboxSadharonSastho);
        resetWork();
    }

    private void resetWork(){
        resetDocumentSelectionArray();
        resetQueryArray();
    }

    private void resetQueryArray(){
        DocumentActivity.queryURL = "getContent.php?tipscat=";
    }

    private void resetDocumentSelectionArray(){
        Arrays.fill(DocumentActivity.selection, false);
    }

    private boolean atLeastOneItemIsSelected(){
        for(int i = 0; i < DocumentActivity.selection.length; ++i){
            if(DocumentActivity.selection[i]) return true;
        }
        return false;
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
                if(atLeastOneItemIsSelected()) {
                   // saveOnSharedPreference();
                    loadSelectedFragment();
                }
                else Toast.makeText(getContext(), "You have to select at least one item", Toast.LENGTH_SHORT).show();
            }
        });
        somporko.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DocumentActivity.selection[0] = b;
            }
        });
        valobasha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DocumentActivity.selection[1] = b;
            }
        });
        premersomoporko.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DocumentActivity.selection[2] = b;
            }
        });
        nijerunnoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DocumentActivity.selection[3] = b;
            }
        });
        vromon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DocumentActivity.selection[4] = b;
            }
        });
        sundorjotok.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DocumentActivity.selection[5] = b;
            }
        });
        sadharonsastho.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DocumentActivity.selection[6] = b;
            }
        });
    }

    private void loadSelectedFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .replace(R.id.fragmentContainerDocument, new SelectedFragment())
                .commit();
    }

    /// ------ Shared Preferences--------
    /*public void saveOnSharedPreference(){
        Gson gson = new Gson();
        String jSon = gson.toJson(DocumentActivity.selection);
        SharedPreferences sharedPreferences = new AppPresenter().getSharedPrefInterface(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Selection Array", jSon);
        editor.apply();
    }*/

    private void loadAccountFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_in_un, R.anim.fragment_out_un)
                .replace(R.id.fragmentContainerDocument, new AccountFragment())
                .commit();
    }
}
