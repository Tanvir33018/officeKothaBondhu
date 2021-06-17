package net.islbd.kothabondhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/*import com.softbd.aamarpay.PayByAamarPay;
import com.softbd.aamarpay.interfaces.OnPaymentRequestListener;
import com.softbd.aamarpay.model.OptionalFields;
import com.softbd.aamarpay.model.PaymentResponse;
import com.softbd.aamarpay.model.RequiredFields;
import com.softbd.aamarpay.utils.Params;*/

import java.util.List;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.model.pojo.PackageInfo;
import net.islbd.kothabondhu.model.pojo.PackageStatusInfo;
import net.islbd.kothabondhu.model.pojo.PackageStatusQuery;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.presenter.IDbInteractor;
import net.islbd.kothabondhu.ui.activity.PaymentMethodActivity;
import net.islbd.kothabondhu.ui.fragment.PackageListFragment;
import net.islbd.kothabondhu.utility.GlobalConstants;
import net.islbd.kothabondhu.utility.HttpStatusCodes;
import net.islbd.kothabondhu.utility.SharedPrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by UserStatusDetails on 2/16/2019.
 */

public class PackageListAdapter extends RecyclerView.Adapter<PackageListAdapter.ViewHolder> implements PackageListFragment.ContinueWork {
    private static final String TAG = "PackageListAdapter";
    private IDbInteractor dbInteractor;
    private Context context;
    private List<PackageInfo> packageList;
    boolean isActive = false;
    private SharedPreferences sharedPreferences;
    private Call<PackageStatusInfo> packageStatusInfoCall;
    private IApiInteractor apiInteractor;
    public String packageId, packageIdentifier, packageDetails, packageDuration, packageMedia;
    private Fragment fragment;
    private int selected_position = 0;
    public String packageDetail;



    public PackageListAdapter(Context context, IDbInteractor dbInteractor, Fragment fragment) {
        this.dbInteractor = dbInteractor;
        this.context = context;
        this.fragment = fragment;
        AppPresenter appPresenter = new AppPresenter();
        this.sharedPreferences = appPresenter.getSharedPrefInterface(context);
        this.apiInteractor = appPresenter.getApiInterface();
        this.isActive = false;
    }

    @Override
    public PackageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_packages_item, parent, false);
        context = parent.getContext();
        return new PackageListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PackageListAdapter.ViewHolder holder, int position) {

        //change 6/14/2021
        /*if(position == selected_position) holder.setPackageTextColorToGreen();
        else holder.setPackageTextColorToBlack();*/

        //holder.packageNameTextView.setText(packageIdentifier);
        holder.packageNameTextView.setText(packageList.get(position).getPackageDetails());

        holder.packageListRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_position = position;
                notifyDataSetChanged();
                loadPackageDetails(position);
               // ((PackageListFragment)fragment).setPackageTextAcToAmount(packageList.get(position).getPackageDetails());
                packageDetail = packageList.get(position).getPackageDetails();
                Log.d("TAG", "onClick: Check Values ");
                ((PackageListFragment) fragment).customerAmount(packageDetail);
                ((PackageListFragment)fragment).eventListener();
                ((PackageListFragment)fragment).mDialog.showDialog();
            }
        });
    }

    @Override
    public void loadMoveToPurchase(){
        String endUserRegId = sharedPreferences.getString(SharedPrefUtils._PACKAGE_IDENTIFIER, "");
        if (endUserRegId.isEmpty()) {
            moveToPurchase(packageId, packageIdentifier, packageMedia, packageDuration, packageDetails);
            return;
        }

        PackageStatusQuery packageStatusQuery = new PackageStatusQuery();
        packageStatusQuery.setEndUserRegId(endUserRegId);
        packageStatusInfoCall = apiInteractor.getPackageStatus(packageStatusQuery);
        packageStatusInfoCall.enqueue(new Callback<PackageStatusInfo>() {
            @Override
            public void onResponse(retrofit2.Call<PackageStatusInfo> rCall, Response<PackageStatusInfo> response) {
                if (response.code() == HttpStatusCodes.OK) {
                    moveToPurchase(packageId, packageIdentifier, packageMedia, packageDuration, packageDetails);
                    Toast.makeText(context, "Package purchase successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Server error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PackageStatusInfo> rCall, Throwable t) {
                //Toast.makeText(context, "Package purchase failed, please try again later", Toast.LENGTH_LONG).show();
                moveToPurchase(packageId, packageIdentifier, packageMedia, packageDuration, packageDetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (packageList == null) {
            return 0;
        }
        return packageList.size();
    }

    public void setPackageList(List<PackageInfo> packageList) {
        this.packageList = packageList;
        if(this.packageList != null && packageList.size() > 0) loadPackageDetails(0);
    }

    private void loadPackageDetails(int position){
        packageId = packageList.get(position).getPkgId();
        packageIdentifier = packageList.get(position).getPkgid();
        packageDetails = packageList.get(position).getPackageDetails();
        packageDuration = packageList.get(position).getPkgDuration();
        packageMedia = packageList.get(position).getMedia();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView packageNameTextView;
        protected RelativeLayout packageListRelativeLayout;

        ViewHolder(View itemView) {
            super(itemView);
            initializeWidgets(itemView);
        }

        private void initializeWidgets(View itemView) {
            packageNameTextView = itemView.findViewById(R.id.package_buy_textView);
            packageListRelativeLayout = itemView.findViewById(R.id.package_item_relativeLayout);
        }

        private void setPackageTextColorToGreen(){
            packageNameTextView.setTextColor(context.getResources().getColor(R.color.green_500));
        }

        //change 6/14/2021
        /*private void setPackageTextColorToBlack(){
            packageNameTextView.setTextColor(context.getResources().getColor(R.color.black));
        }*/
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

}
