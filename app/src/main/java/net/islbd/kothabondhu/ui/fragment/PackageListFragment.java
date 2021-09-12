package net.islbd.kothabondhu.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.aamarpay.library.AamarPay;
import com.aamarpay.library.DialogBuilder;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/*import com.softbd.aamarpay.PayByAamarPay;
import com.softbd.aamarpay.interfaces.OnPaymentRequestListener;
import com.softbd.aamarpay.model.OptionalFields;
import com.softbd.aamarpay.model.PaymentResponse;
import com.softbd.aamarpay.model.RequiredFields;
import com.softbd.aamarpay.utils.Params;*/

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.model.pojo.AamarPayPostInfo;
import net.islbd.kothabondhu.model.pojo.NagadResponse;
import net.islbd.kothabondhu.model.pojo.PackageInfo;
import net.islbd.kothabondhu.model.pojo.PackageInfoQuery;
import net.islbd.kothabondhu.model.pojo.PackageStatusInfo;
import net.islbd.kothabondhu.model.pojo.PackageStatusQuery;
import net.islbd.kothabondhu.model.pojo.UserAccountInfo;
import net.islbd.kothabondhu.model.pojo.UserGmailInfo;
import net.islbd.kothabondhu.model.pojo.UserQuery;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.presenter.IDbInteractor;
import net.islbd.kothabondhu.ui.activity.PackagesActivity;
import net.islbd.kothabondhu.ui.activity.PaymentMethodActivity;
import net.islbd.kothabondhu.ui.adapter.PackageListAdapter;
import net.islbd.kothabondhu.utility.GlobalConstants;
import net.islbd.kothabondhu.utility.HttpStatusCodes;
import net.islbd.kothabondhu.utility.ProgressDialogBox;
import net.islbd.kothabondhu.utility.SharedPrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class PackageListFragment extends Fragment {
    private RecyclerView packageListRecyclerView;
    private Context context;
    private IDbInteractor dbInteractor;
    private IApiInteractor apiInteractor;
    private Call<List<PackageInfo>> packageListCall;
    private Call<PackageStatusInfo> packageStatusInfoCall;
    private Call<AamarPayPostInfo> aamarPayPostInfoCall;
    private AamarPayPostInfo aamarPayPostInfo;
    private Gson gson;
    private SharedPreferences sharedPref;
    private PackageListAdapter packageListAdapter;
    private TextView textViewAll, textViewTitle;
    private Button buttonContinue;
    private PaymentMethodActivity paymentMethodActivity;
    private UserGmailInfo userGmailInfo;
    public static ProgressDialogBox mDialog;
    public static boolean mNagad = false, mOthers = false;
    public Dialog mPaymentMethodDialog;
    public String packageDetail;


    // ****Aamarpay varriable****
    private AlertDialog.Builder builder;
    private AamarPay aamarPay;
    private Button payNow;
    private DialogBuilder dialogBuilder;
    private String trxID, trxAmount = "40", trxCurrency, customerName, customerEmail, customerPhone, customerAddress, customerCity,
                   customerCountry, paymentDescription = "FORTY_TAKA";


    private static final String FORTY_TAKA = "২০ মিনিট / ৪০ টাকা";
    private static final String HUNDRED_TAKA = "৫০ মিনিট / ১০০ টাকা";
    private static final String HUNDRED_AND_NINTY_TAKA = "১০০ মিনিট / ১৯০ টাকা";
    private static final String THREE_HUNDRED_AND_EIGHTY_TAKA = "২০০ মিনিট / ৩৮০ টাকা";





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_package_list, container, false);
        initializeWidgets(view);
        initializeData();
        requiredFieldInit();
        if(PackagesActivity.nagadFlag){
            mDialog.showDialog();
            getNagadResponse();
        }
        //eventListener();
        return view;
    }
    /*public interface ContinueWork{
        void loadMoveToPurchase();
    }

    private ContinueWork continueWork;*/


    private void getNagadResponse() {
        apiInteractor.getNagadResponse(PackagesActivity.mOrderId, PackagesActivity.mAmount).enqueue(new Callback<NagadResponse>() {
            @Override
            public void onResponse(Call<NagadResponse> call, Response<NagadResponse> response) {
                if(response.isSuccessful()){
                    PackagesActivity.nagadFlag = false;
                    if(response.body().getStatus().equals("Faild")){
                        Toast.makeText(context, "Payment Not Complete!", Toast.LENGTH_SHORT).show();
                        mDialog.dismissDialog();

                    }else if(response.body().getStatus().equals("Success")){
                        //continueWork.loadMoveToPurchase();
                        loadMoveToPurchase();
                        mDialog.dismissDialog();
                    }

                }else{
                    Toast.makeText(context, "Network Error PLF!", Toast.LENGTH_SHORT).show();
                    mDialog.dismissDialog();
                }
            }

            @Override
            public void onFailure(Call<NagadResponse> call, Throwable t) {
                Toast.makeText(context, "Network Error PLF!", Toast.LENGTH_SHORT).show();
                mDialog.dismissDialog();

            }
        });
    }




    public void eventListener(){
               // dialogBuilder.showLoading();
                Log.d("TAG", "onClick: ");
                // Set transaction parameter
                aamarPay.setTransactionParameter(trxAmount, trxCurrency, paymentDescription);
                // Set Customer details
                aamarPay.setCustomerDetails(customerName, customerEmail, customerPhone, customerAddress, customerCity, customerCountry);
                // Initiating PGW
                aamarPay.initPGW(new AamarPay.onInitListener() {


                    @Override
                    public void onInitFailure(Boolean error, String message) {
                        // You will get the response, if payment gateway initialization is failed.
                        Log.d("TEST_IF", message);
                        Toast.makeText(context, "Initialization Failed!", Toast.LENGTH_SHORT).show();
                        mDialog.dismissDialog();


                    }

                    @Override
                    public void onPaymentSuccess(JSONObject jsonObject) {
                        // You will get the payment success response as a JSON callback
                        Log.d("TEST_PS", jsonObject.toString());

                        Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                        aamarPayPostInfo = gson.fromJson(jsonObject.toString(), AamarPayPostInfo.class);
                        aamarPayPostInfoCall = apiInteractor.setAamarPay(aamarPayPostInfo);
                        aamarPayPostInfoCall.enqueue(new Callback<AamarPayPostInfo>() {
                            @Override
                            public void onResponse(Call<AamarPayPostInfo> call, Response<AamarPayPostInfo> response) {
                                Log.d("TAG", "onResponse: " +response);
                                mDialog.dismissDialog();
                            }

                            @Override
                            public void onFailure(Call<AamarPayPostInfo> call, Throwable t) {
                                Log.d("TAG", "onFailure: "+t);

                            }
                        });
                        //continueWork.loadMoveToPurchase();
                        loadMoveToPurchase();

                    }

                    @Override
                    public void onPaymentFailure(JSONObject jsonObject) {
                        // You will get the payment failed response as a JSON callback
                        Log.d("TEST_PF", jsonObject.toString());
                        Toast.makeText(context, "Failed!!", Toast.LENGTH_SHORT).show();

                        aamarPayPostInfo = gson.fromJson(jsonObject.toString(), AamarPayPostInfo.class);
                        aamarPayPostInfoCall = apiInteractor.setAamarPay(aamarPayPostInfo);
                        aamarPayPostInfoCall.enqueue(new Callback<AamarPayPostInfo>() {
                            @Override
                            public void onResponse(Call<AamarPayPostInfo> call, Response<AamarPayPostInfo> response) {
                                Toast.makeText(context, "Server Post Successfull", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<AamarPayPostInfo> call, Throwable t) {

                                Toast.makeText(context, "Server Post Failure", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    @Override
                    public void onPaymentProcessingFailed(JSONObject jsonObject) {
                        // You will get the payment processing failed response as a JSON callback
                        Log.d("TEST_PPF", jsonObject.toString());
                        Toast.makeText(context, "Processing Failed", Toast.LENGTH_SHORT).show();

                        aamarPayPostInfo = gson.fromJson(jsonObject.toString(), AamarPayPostInfo.class);
                        aamarPayPostInfoCall = apiInteractor.setAamarPay(aamarPayPostInfo);
                        aamarPayPostInfoCall.enqueue(new Callback<AamarPayPostInfo>() {
                            @Override
                            public void onResponse(Call<AamarPayPostInfo> call, Response<AamarPayPostInfo> response) {
                                Toast.makeText(context, "Server Post Successfull", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<AamarPayPostInfo> call, Throwable t) {

                                Toast.makeText(context, "Server Post Failure", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }

                    @Override
                    public void onPaymentCancel(JSONObject jsonObject) {
                        // You will get the payment cancel response as a JSON callback
                        Log.d("TEST", jsonObject.toString());
                        try {
                            // Call the transaction verification check validity
                            aamarPay.getTransactionInfo(jsonObject.getString("trx_id"), new AamarPay.TransactionInfoListener() {
                                @Override
                                public void onSuccess(JSONObject jsonObject) {
                                    Log.d("TEST_", jsonObject.toString());
                                    Toast.makeText(context, "Payment Canceled", Toast.LENGTH_SHORT).show();
                                    mDialog.dismissDialog();

                                }

                                @Override
                                public void onFailure(Boolean error, String message) {
                                    Toast.makeText(context, "Cancellation Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (JSONException e) { e.printStackTrace(); }
                    }
                });
    }



    public void customerAmount(String description) {

        paymentDescription = description;
        String amount = "";
        switch (description){
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
        trxAmount = amount;
        PackagesActivity.mAmount = amount;
    }


    public void loadMoveToPurchase(){
        String endUserRegId = sharedPref.getString(SharedPrefUtils._PACKAGE_IDENTIFIER, "");
        if (endUserRegId.isEmpty()) {
            moveToPurchase(PackagesActivity.packageId, PackagesActivity.packageIdentifier, PackagesActivity.packageMedia,
                    PackagesActivity.packageDuration, PackagesActivity.packageDetails);
            return;
        }

        PackageStatusQuery packageStatusQuery = new PackageStatusQuery();
        packageStatusQuery.setEndUserRegId(endUserRegId);
        packageStatusInfoCall = apiInteractor.getPackageStatus(packageStatusQuery);
        packageStatusInfoCall.enqueue(new Callback<PackageStatusInfo>() {
            @Override
            public void onResponse(retrofit2.Call<PackageStatusInfo> rCall, Response<PackageStatusInfo> response) {
                if (response.code() == HttpStatusCodes.OK) {
                    moveToPurchase(PackagesActivity.packageId, PackagesActivity.packageIdentifier, PackagesActivity.packageMedia,
                            PackagesActivity.packageDuration, PackagesActivity.packageDetails);

                    Toast.makeText(context, "Package purchase successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Server error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PackageStatusInfo> rCall, Throwable t) {
                //Toast.makeText(context, "Package purchase failed, please try again later", Toast.LENGTH_LONG).show();
                moveToPurchase(PackagesActivity.packageId, PackagesActivity.packageIdentifier,
                        PackagesActivity.packageMedia, PackagesActivity.packageDuration, PackagesActivity.packageDetails);
            }
        });
    }

    private void moveToPurchase(String packageId, String packageIdentifier, String packageMedia, String packageDuration, String packageDetails) {
        Intent intent = new Intent(context, PaymentMethodActivity.class);
        intent.putExtra(GlobalConstants.EXT_TAG_PACKAGE_ID, packageId);
        intent.putExtra(GlobalConstants.EXT_TAG_PACKAGE_IDENTIFIER, packageIdentifier);
        intent.putExtra(GlobalConstants.EXT_TAG_PACKAGE_MEDIA, packageMedia);
        intent.putExtra(GlobalConstants.EXT_TAG_PACKAGE_DURATION, packageDuration);
        intent.putExtra(GlobalConstants.EXT_TAG_PACKAGE_DETAILS, packageDetails);
        intent.putExtra("abc", true);
        context.startActivity(intent);
    }


    private void requiredFieldInit(){
        // Initiate payment
        aamarPay = new AamarPay(context, "kothabondhu", getString(R.string.AmarPay_SignatureKey));

        // Set Test Mode
        aamarPay.testMode(false);

        aamarPay.autoGenerateTransactionID(true);
        trxID = aamarPay.generate_trx_id();
        trxCurrency = "BDT";
        customerAddress = "Mirpur Pallabi";
        customerCity = "Dhaka";
        customerCountry = "Bangladesh";

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
                customerPhone = "0"+userAccountInfo.getUserInfo().getPhoneNumber();

                Log.d("TAG", "onClick: Check Values ");
            }
            @Override
            public void onFailure(Call<UserAccountInfo> call, Throwable t) {
                Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
            }
        });

        customerEmail = getUserInfoFromGMail().getEmail();

    }


 
    private UserGmailInfo getUserInfoFromGMail(){
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getContext()); //It will return null on sign out condition
        if(googleSignInAccount != null){
            String name = googleSignInAccount.getDisplayName();
            String email = googleSignInAccount.getEmail();
            String id = googleSignInAccount.getId();
            return new UserGmailInfo(name, email, id);
        }
        return null;
    }

    private void initializeWidgets(View view) {
        packageListRecyclerView = view.findViewById(R.id.package_list_recyclerView);
        textViewAll = view.findViewById(R.id.textViewAllPackageList);
        textViewTitle = view.findViewById(R.id.textViewTitlePackageList);
        //buttonContinue = view.findViewById(R.id.buttonContinuePackageList);
    }

    private void initializeData() {
        context = this.getContext();
        mDialog = new ProgressDialogBox(context);
        mPaymentMethodDialog = new Dialog(getActivity());
        mPaymentMethodDialog.setContentView(R.layout.payment_method_dialog);
        AppPresenter appPresenter = new AppPresenter();
        dbInteractor = appPresenter.getDbInterface(context);
        apiInteractor = appPresenter.getApiInterface();
        sharedPref = appPresenter.getSharedPrefInterface(context);
        aamarPayPostInfo = new AamarPayPostInfo();
        gson = new GsonBuilder().create();
        createList();
    }

    public void showPaymentMethods(){
        Log.d("TAG", "showPaymentMethods: ");
        mPaymentMethodDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPaymentMethodDialog.show();
        mPaymentMethodDialog.findViewById(R.id.nagadLayout).setOnClickListener(v -> {
            //Toast.makeText(context, "Nagad Payment", Toast.LENGTH_SHORT).show();
            PackagesActivity.flag = true;
            mPaymentMethodDialog.dismiss();
            customerAmount(packageDetail);
            mDialog.showDialog();
            loadNagadWebView();
            PackagesActivity.nagadFlag = true;

        });
        mPaymentMethodDialog.findViewById(R.id.bkashLayout).setOnClickListener(v -> {
            //Toast.makeText(context, "Bkash Payment", Toast.LENGTH_SHORT).show();
            PackagesActivity.flag = true;
            mPaymentMethodDialog.dismiss();
            customerAmount(packageDetail);
            eventListener();
            mDialog.showDialog();
        });
        mPaymentMethodDialog.findViewById(R.id.cancelButton).setOnClickListener(v -> {
            mPaymentMethodDialog.dismiss();
        });
    }

    private void loadNagadWebView() {
        generateOrderId();
        Bundle mBundle = new Bundle();
        mBundle.putString("mOrderId",PackagesActivity.mOrderId);
        mBundle.putString("amount",trxAmount); //trxAmount
        Fragment fragment = new WebViewFragment();
        fragment.setArguments(mBundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerPackage,fragment)
                .commit();

    }

    private void createList() {
        packageListAdapter = new PackageListAdapter(context, dbInteractor, this);
        //continueWork = (ContinueWork)packageListAdapter;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
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

    private void generateOrderId() {
        PackagesActivity.mOrderId = "KPAK"+ getAlphaNumericString(14);
    }

    static String getAlphaNumericString(int n)
    {
        String AlphaNumericString =  "0123456789";

        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
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
