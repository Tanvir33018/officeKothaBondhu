package net.islbd.kothabondhu.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
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

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.model.pojo.AamarPayPostInfo;
import net.islbd.kothabondhu.model.pojo.PackageInfo;
import net.islbd.kothabondhu.model.pojo.PackageInfoQuery;
import net.islbd.kothabondhu.model.pojo.UserAccountInfo;
import net.islbd.kothabondhu.model.pojo.UserGmailInfo;
import net.islbd.kothabondhu.model.pojo.UserQuery;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.presenter.IDbInteractor;
import net.islbd.kothabondhu.ui.activity.PaymentMethodActivity;
import net.islbd.kothabondhu.ui.adapter.PackageListAdapter;
import net.islbd.kothabondhu.utility.HttpStatusCodes;

import org.json.JSONException;
import org.json.JSONObject;

public class PackageListFragment extends Fragment {
    private RecyclerView packageListRecyclerView;
    private Context context;
    private IDbInteractor dbInteractor;
    private IApiInteractor apiInteractor;
    private Call<List<PackageInfo>> packageListCall;
    private Call<AamarPayPostInfo> aamarPayPostInfoCall;
    private AamarPayPostInfo aamarPayPostInfo;
    private Gson gson;
    private SharedPreferences sharedPref;
    private PackageListAdapter packageListAdapter;
    private TextView textViewAll, textViewTitle;
    private Button buttonContinue;
    private PaymentMethodActivity paymentMethodActivity;
    private UserGmailInfo userGmailInfo;


    // ****Aamarpay varriable****
    private AlertDialog.Builder builder;
    private AamarPay aamarPay;
    private Button payNow;
    private DialogBuilder dialogBuilder;
    private String trxID, trxAmount = "40", trxCurrency, customerName, customerEmail, customerPhone, customerAddress, customerCity,
                   customerCountry, paymentDescription = "20 min / 40 TK";


    private static final String FORTY_TAKA = "20 min / 40 TK";
    private static final String HUNDRED_TAKA = "50 min / 100 TK";
    private static final String HUNDRED_AND_NINTY_TAKA = "100 min / 190 TK";
    private static final String THREE_HUNDRED_AND_EIGHTY_TAKA = "200 min / 380 TK";





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_package_list, container, false);
        initializeWidgets(view);
        initializeData();
        requiredFieldInit();
        eventListener();
        return view;
    }



    public interface ContinueWork{
        void loadMoveToPurchase();
    }

    private ContinueWork continueWork;

    private void eventListener(){


        buttonContinue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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
                            }

                            @Override
                            public void onFailure(Call<AamarPayPostInfo> call, Throwable t) {
                                Log.d("TAG", "onFailure: "+t);

                            }
                        });
                        continueWork.loadMoveToPurchase();

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
        trxAmount =  amount;
    }


    private void requiredFieldInit(){
        // Initiate payment
        aamarPay = new AamarPay(context, "kothabondhu", "b1a37fb12afb8571fe11a485349d99aa");

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
        buttonContinue = view.findViewById(R.id.buttonContinuePackageList);
    }

    private void initializeData() {
        context = this.getContext();
        AppPresenter appPresenter = new AppPresenter();
        dbInteractor = appPresenter.getDbInterface(context);
        apiInteractor = appPresenter.getApiInterface();
        sharedPref = appPresenter.getSharedPrefInterface(context);
        aamarPayPostInfo = new AamarPayPostInfo();
        gson = new GsonBuilder().create();
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
