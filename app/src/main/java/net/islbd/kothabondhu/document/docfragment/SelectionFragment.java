package net.islbd.kothabondhu.document.docfragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Context;
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
import net.islbd.kothabondhu.ui.activity.HomeTabActivity;
import net.islbd.kothabondhu.utility.MySharedPreferences;

import java.util.Arrays;

public class SelectionFragment extends Fragment {
    private Button buttonBack;
    private Button buttonUnderstand;
    private CheckBox somporko, valobasha, premersomoporko, nijerunnoti, vromon, sundorjotok, sadharonsastho;
    public SharedPreferences mySharedPreferences;
    private SharedPreferences.Editor editor;
    public final String SOMPORKO = "Somporko";
    public final String VALOBASA = "Valobasa";
    public final String PREMERSOMPORKO = "PremerSomporko";
    public final String NIJERUNNOTI = "NijerUnnoti";
    public final String VROMON = "vromon";
    public final String SUNDORJO = "Sundorjo";
    public final String SHADHARONSASTHO = "SadharonSastho";
    private DocumentActivity documentActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.document_selection_fragment_list_item, container, false);

        init(view);


        if(mySharedPreferences.getBoolean(SOMPORKO,false)) {
            somporko.setChecked(true);
            DocumentActivity.selection[0] = true;
        }
        if(mySharedPreferences.getBoolean(VALOBASA, false)) {
            valobasha.setChecked(true);
            DocumentActivity.selection[1] = true;
        }
        if(mySharedPreferences.getBoolean(PREMERSOMPORKO,false)){
            premersomoporko.setChecked(true);
            DocumentActivity.selection[2] = true;
        }
        if(mySharedPreferences.getBoolean(NIJERUNNOTI,false)){
            nijerunnoti.setChecked(true);
            DocumentActivity.selection[3] = true;
        }
        if(mySharedPreferences.getBoolean(VROMON,false)) {
            vromon.setChecked(true);
            DocumentActivity.selection[4] = true;
        }
        if(mySharedPreferences.getBoolean(SUNDORJO,false)) {
            sundorjotok.setChecked(true);
            DocumentActivity.selection[5] = true;
        }
        if(mySharedPreferences.getBoolean(SHADHARONSASTHO,false)) {
            sadharonsastho.setChecked(true);
            DocumentActivity.selection[6] = true;
        }

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
        mySharedPreferences = getContext().getSharedPreferences("SharedPref",Context.MODE_PRIVATE);
        editor = mySharedPreferences.edit();
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

                if(mySharedPreferences.getBoolean(SOMPORKO,false)  ||
                        mySharedPreferences.getBoolean(VALOBASA, false) ||
                        mySharedPreferences.getBoolean(PREMERSOMPORKO,false) ||
                        mySharedPreferences.getBoolean(NIJERUNNOTI,false) ||
                        mySharedPreferences.getBoolean(VROMON,false) ||
                        mySharedPreferences.getBoolean(SUNDORJO,false) ||
                        mySharedPreferences.getBoolean(SHADHARONSASTHO,false)) {

                    Intent intent = new Intent(getContext(), HomeTabActivity.class);
                    /*intent.putExtra("From_Selection_Fragment", 1);*/
                    if(intent != null) startActivity(intent);

                }else {
                    loadAccountFragment();
                }
            }
        });
        buttonUnderstand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(atLeastOneItemIsSelected()) {
                    if(somporko.isChecked()) editor.putBoolean(SOMPORKO,somporko.isChecked());
                    if(valobasha.isChecked()) editor.putBoolean(VALOBASA, valobasha.isChecked());
                    if(premersomoporko.isChecked()) editor.putBoolean(PREMERSOMPORKO, premersomoporko.isChecked());
                    if(nijerunnoti.isChecked()) editor.putBoolean(NIJERUNNOTI, nijerunnoti.isChecked());
                    if(vromon.isChecked()) editor.putBoolean(VROMON, vromon.isChecked());
                    if(sundorjotok.isChecked()) editor.putBoolean(SUNDORJO, sundorjotok.isChecked());
                    if(sadharonsastho.isChecked()) editor.putBoolean(SHADHARONSASTHO, sadharonsastho.isChecked());

                    if(!somporko.isChecked()) editor.putBoolean(SOMPORKO,false);
                    if(!valobasha.isChecked()) editor.putBoolean(VALOBASA, false);
                    if(!premersomoporko.isChecked()) editor.putBoolean(PREMERSOMPORKO, false);
                    if(!nijerunnoti.isChecked()) editor.putBoolean(NIJERUNNOTI, false);
                    if(!vromon.isChecked()) editor.putBoolean(VROMON, false);
                    if(!sundorjotok.isChecked()) editor.putBoolean(SUNDORJO, false);
                    if(!sadharonsastho.isChecked()) editor.putBoolean(SHADHARONSASTHO, false);

                    editor.apply();
                    DocumentActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                    DocumentActivity.bottomNavigationView.setSelectedItemId(R.id.bottom_home);
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


    private void loadAccountFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_in_un, R.anim.fragment_out_un)
                .replace(R.id.fragmentContainerDocument, new AccountFragment())
                .commit();
    }
}
