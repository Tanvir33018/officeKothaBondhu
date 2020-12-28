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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.model.pojo.UserGmailInfo;
import net.islbd.kothabondhu.ui.activity.HomeTabActivity;
import net.islbd.kothabondhu.ui.activity.LoginActivity;

public class WelcomeFragment extends Fragment {
    private Button buttonTour;
    private Button buttonNotNeed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.document_welcome_fragment_list_item, container, false);
        init(view);
        eventListeners();
        return view;
    }
    private void init(View view){
        buttonTour = view.findViewById(R.id.buttonTour);
        buttonNotNeed = view.findViewById(R.id.buttonNotNeed);
    }
    private void eventListeners(){
        buttonTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadQuestionFragment();
            }
        });
        buttonNotNeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getUserInfoFromGMail() != null) loadHomeTabActivity();
                else loadLoginActivity();
            }
        });
    }
    private UserGmailInfo getUserInfoFromGMail(){
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getContext()); //It will return null on sign out condition
        if(googleSignInAccount != null){
            String name = googleSignInAccount.getDisplayName();
            String email = googleSignInAccount.getEmail();
            String id = googleSignInAccount.getId();
            return new UserGmailInfo(name, email, id);
            //Log.d(TAG, "onCreate: " + email);
        }
        return null;
    }
    private void loadQuestionFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .replace(R.id.fragmentContainerDocument, new QuestionFragment())
                .commit();
    }
    private void loadHomeTabActivity(){
        Intent intent = new Intent(getActivity(), HomeTabActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
    private void loadLoginActivity(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
