package net.islbd.kothabondhu.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.softbd.aamarpay.PayByAamarPay;
import com.softbd.aamarpay.interfaces.OnPaymentRequestListener;
import com.softbd.aamarpay.model.OptionalFields;
import com.softbd.aamarpay.model.PaymentResponse;
import com.softbd.aamarpay.model.RequiredFields;
import com.softbd.aamarpay.utils.Params;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.model.pojo.PackageInfo;
import net.islbd.kothabondhu.model.pojo.PackageInfoQuery;
import net.islbd.kothabondhu.model.pojo.UserAccountInfo;
import net.islbd.kothabondhu.model.pojo.UserGmailInfo;
import net.islbd.kothabondhu.model.pojo.UserQuery;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.presenter.IDbInteractor;
import net.islbd.kothabondhu.ui.activity.HomeTabActivity;
import net.islbd.kothabondhu.ui.activity.PaymentMethodActivity;
import net.islbd.kothabondhu.ui.adapter.PackageListAdapter;
import net.islbd.kothabondhu.utility.HttpStatusCodes;
import net.islbd.kothabondhu.utility.SharedPrefUtils;

public class PackageListFragment extends Fragment {
    private RecyclerView packageListRecyclerView;
    private Context context;
    private IDbInteractor dbInteractor;
    private IApiInteractor apiInteractor;
    private Call<List<PackageInfo>> packageListCall;
    private SharedPreferences sharedPref;
    private PackageListAdapter packageListAdapter;
    private TextView textViewAll, textViewTitle;
    private Button buttonContinue;
    private PaymentMethodActivity paymentMethodActivity;
    private UserGmailInfo userGmailInfo;
    public static String payStatus = "Null Value";

    //Payment Library variable
    private String customerName = "";
    private String customerEmail;
    private String customerAddress1 = "1/11 Mirpur Pallabi";
    private String customerCity = "Dhaka";
    private String customerState = "Dhaka";
    private String customerZipCode = "1216";
    private String customerCountry = "Bangladesh";
    private String customerMobileNo;
    private String customerPayDescription;
    private String customerPayAmount;
    private String customerTranId;
    private String successUrl = "https://sandbox.aamarpay.com/success.php";
    private String failUrl = "https://sandbox.aamarpay.com/failed.php";
    private String cancelUrl = "https://sandbox.aamarpay.com/failed.php";


    private static final String FORTY_TAKA = "20 min / 40 TK";
    private static final String HUNDRED_TAKA = "50 min / 100 TK";
    private static final String HUNDRED_AND_NINTY_TAKA = "100 min / 190 TK";
    private static final String THREE_HUNDRED_AND_EIGHTY_TAKA = "200 min / 380 TK";

    public interface ContinueWork{
        void loadMoveToPurchase();
    }

    private ContinueWork continueWork;

    private void eventListener(){
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerPayAmount = customerAmount(packageListAdapter.amount);
                customerPayDescription = packageListAdapter.amount;
                Log.d("TAG", "onClick: Check Values ");
                aamarPayPayment();

            }
        });
    }





    private String customerAmount(String payAmount) {
        String amount = "";
        switch (payAmount){
            case HUNDRED_TAKA:
                amount =  "100";
                break;
            case HUNDRED_AND_NINTY_TAKA:
                amount = "190";
                break;
            case THREE_HUNDRED_AND_EIGHTY_TAKA:
                amount = "380";
                break;
            case FORTY_TAKA:
                amount = "40";
                break;
        }
        return amount;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_package_list, container, false);
        initializeWidgets(view);
        initializeData();
        requiredFieldInit();
        eventListener();
        return view;
    }


    private void requiredFieldInit(){
        UserQuery userQuery = new UserQuery();
        String id = getUserInfoFromGMail().getId();
        userQuery.setEndUserId(id);
        userQuery.setEndUserId(getUserInfoFromGMail().getId());
        apiInteractor.getUserAccountInfo(userQuery).enqueue(new Callback<UserAccountInfo>() {
            @Override
            public void onResponse(Call<UserAccountInfo> call, Response<UserAccountInfo> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(context, "Having Problem", Toast.LENGTH_SHORT).show();
                }
                UserAccountInfo userAccountInfo = response.body();

                customerName = userAccountInfo.getUserInfo().getName();
                customerMobileNo = "0"+userAccountInfo.getUserInfo().getPhoneNumber();

                Log.d("TAG", "onClick: Check Values ");
            }
            @Override
            public void onFailure(Call<UserAccountInfo> call, Throwable t) {
                Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
            }
        });

        customerTranId = customerName + customerMobileNo;
        customerEmail = getUserInfoFromGMail().getEmail();




    }

    private void aamarPayPayment() {
        RequiredFields requiredFields = new RequiredFields(
                customerName,
                customerEmail,
                customerAddress1,
                customerCity,
                customerState,
                customerZipCode,
                customerCountry,
                customerMobileNo,
                customerPayDescription,
                customerPayAmount,
                Params.CURRENCY_BDT,
                customerTranId,
                "kothabondhu",
                "b1a37fb12afb8571fe11a485349d99aa",
                "https://sandbox.aamarpay.com/success.php",
                "https://sandbox.aamarpay.com/failed.php",
                "https://sandbox.aamarpay.com/failed.php"
        );
        OptionalFields optionalFields = new OptionalFields();
        /*PayByAamarPay payByAamarpay = new PayByAamarPay(
                mContext,
                requiredFields,
                optionalFields
        );*/
        PayByAamarPay.getInstance(getActivity(), requiredFields,
                optionalFields).payNow(new OnPaymentRequestListener() {
            @Override
            public void onPaymentResponse(int paymentStatus, PaymentResponse
                    paymentResponse) {
                //Handle your response here like
                //textView.setText(paymentResponse.getPayStatus());
                if(paymentResponse.getPayStatus() == "Successful"){
                    Log.d("TAG", "This is PaymentResponse: "+paymentResponse.getPayStatus());
                    Toast.makeText(context, paymentResponse.getPayStatus(), Toast.LENGTH_SHORT).show();

                    payStatus = paymentResponse.getPayStatus();
                    continueWork.loadMoveToPurchase();
                }
                else{
                    Intent intent = new Intent(getContext(), PaymentMethodActivity.class);
                    startActivity(intent);
                    //finish();
                }

            }
        });
    }

    /*private String getPayStatus(String payStatus) {
        String text = "Failed";
        *//*switch (payStatus){
            case "0":
                text = "Initiated";
                break;
            case "1":
                text = "Attempt";
                break;
            case "2":
                text = "Successful";
                break;
            case "3":
                text = "Canceled";
                break;
            case "4":
                text = "Chargeback";
                break;
            case "5":
                text = "On-Hold";
                break;
            case "6":
                text = "Suspect Fraud";
                break;
            case "7":
                text = "Failed";
                break;
            case "8":
                text = "Refunded-Bank";
                break;
            case "9":
                text = "Incomplete";
                break;
            case "10":
                text = "Refund-Void";
                break;
            case "11":
                text = "Error";
                break;
            case "12":
                text = "ChargeBack-Refund";
                break;
            case "13":
                text = "Missing-Authorised-Email";
                break;
            case "14":
                text = "ChargeBack-Dispute";
                break;
            case "15":
                text = "Settlement Void";
                break;
            case "16":
                text = "Refund-Processing";
                break;
        }*//*
        if(payStatus == "Successfull")text = "Successful";
        return text;
    }*/

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

    private void initializeWidgets(View view) {
        packageListRecyclerView = view.findViewById(R.id.package_list_recyclerView);
        textViewAll = view.findViewById(R.id.textViewAllPackageList);
        textViewTitle = view.findViewById(R.id.textViewTitlePackageList);
        buttonContinue = view.findViewById(R.id.buttonContinuePackageList);
    }

    private void initializeData() {
        context = this.getContext();
        AppPresenter appPresenter = new AppPresenter();
        dbInteractor = appPresenter.getDbInterface(context);
        apiInteractor = appPresenter.getApiInterface();
        sharedPref = appPresenter.getSharedPrefInterface(context);
        createList();
    }

    private void createList() {
        packageListAdapter = new PackageListAdapter(context, dbInteractor, this);
        continueWork = (ContinueWork)packageListAdapter;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        packageListRecyclerView.setLayoutManager(layoutManager);
        packageListRecyclerView.setLayoutManager(layoutManager);
        packageListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        packageListRecyclerView.setAdapter(packageListAdapter);

        downloadPackages();
    }

    public void setPackageTextAcToAmount(String amount){
        switch (amount){
            case FORTY_TAKA:
                textViewTitle.setText(FORTY_TAKA);
                textViewAll.setText(getResources().getString(R.string.amount_40));
                break;
            case HUNDRED_TAKA:
                textViewTitle.setText(HUNDRED_TAKA);
                textViewAll.setText(getResources().getString(R.string.amount_100));
                break;
            case HUNDRED_AND_NINTY_TAKA:
                textViewTitle.setText(HUNDRED_AND_NINTY_TAKA);
                textViewAll.setText(getResources().getString(R.string.amount_190));
                break;
            case THREE_HUNDRED_AND_EIGHTY_TAKA:
                textViewTitle.setText(THREE_HUNDRED_AND_EIGHTY_TAKA);
                textViewAll.setText(getResources().getString(R.string.amount_380));
                break;
            default: break;
        }
    }

    private void downloadPackages() {
        PackageInfoQuery packageInfoQuery = new PackageInfoQuery();
        packageInfoQuery.setQ("19");
        userGmailInfo = getUserInfoFromGMail();
        //packageInfoQuery.setMobilenumber(String.valueOf(sharedPref.getInt(SharedPrefUtils._USER_PHONE, 0)));
        packageInfoQuery.setMobilenumber(userGmailInfo.getId());
        packageListCall = apiInteractor.getPackageList(packageInfoQuery);
        packageListCall.enqueue(new Callback<List<PackageInfo>>() {
            @Override
            public void onResponse(Call<List<PackageInfo>> call, Response<List<PackageInfo>> response) {
                if (response.code() == HttpStatusCodes.OK) {
                    List<PackageInfo> packageInfoList = response.body();
                    if (packageInfoList != null) {
                        packageListAdapter.setPackageList(packageInfoList);
                        packageListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PackageInfo>> call, Throwable t) {

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (context != null) {
            createList();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        createList();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
