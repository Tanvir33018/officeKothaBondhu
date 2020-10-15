package net.islbd.kothabondhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.model.pojo.PackageInfo;
import net.islbd.kothabondhu.model.pojo.PackageStatusInfo;
import net.islbd.kothabondhu.model.pojo.PackageStatusQuery;
import net.islbd.kothabondhu.presenter.AppPresenter;
import net.islbd.kothabondhu.presenter.IApiInteractor;
import net.islbd.kothabondhu.presenter.IDbInteractor;
import net.islbd.kothabondhu.ui.activity.PaymentMethodActivity;
import net.islbd.kothabondhu.utility.GlobalConstants;
import net.islbd.kothabondhu.utility.HttpStatusCodes;
import net.islbd.kothabondhu.utility.SharedPrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by UserStatusDetails on 2/16/2019.
 */

public class PackageListAdapter extends RecyclerView.Adapter<PackageListAdapter.ViewHolder> {
    private static final String TAG = "PackageListAdapter";
    private IDbInteractor dbInteractor;
    private Context context;
    private List<PackageInfo> packageList;
    boolean isActive = false;
    private SharedPreferences sharedPreferences;
    private Call<PackageStatusInfo> packageStatusInfoCall;
    private IApiInteractor apiInteractor;

    public PackageListAdapter(Context context, IDbInteractor dbInteractor) {
        this.dbInteractor = dbInteractor;
        this.context = context;
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
        final String packageId = packageList.get(position).getPkgId();
        final String packageIdentifier = packageList.get(position).getPkgid();
        final String packageDetails = packageList.get(position).getPackageDetails();
        final String packageDuration = packageList.get(position).getPkgDuration();
        final String packageMedia = packageList.get(position).getMedia();
        holder.packageNameTextView.setText(packageIdentifier);

        holder.packageListRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            Toast.makeText(context, "Package already purchased", Toast.LENGTH_SHORT).show();
                        } else {
                            moveToPurchase(packageId, packageIdentifier, packageMedia, packageDuration, packageDetails);
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<PackageStatusInfo> rCall, Throwable t) {
                        moveToPurchase(packageId, packageIdentifier, packageMedia, packageDuration, packageDetails);
                    }
                });


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
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView packageNameTextView;
        protected RelativeLayout packageListRelativeLayout;

        ViewHolder(View itemView) {
            super(itemView);
            initializeWidgets(itemView);
        }

        private void initializeWidgets(View itemView) {
            packageNameTextView = itemView.findViewById(R.id.package_details_textView);
            packageListRelativeLayout = itemView.findViewById(R.id.package_item_relativeLayout);
        }
    }

    private void moveToPurchase(String packageId, String packageIdentifier, String packageMedia, String packageDuration, String packageDetails) {
        Intent intent = new Intent(context, PaymentMethodActivity.class);
        intent.putExtra(GlobalConstants.EXT_TAG_PACKAGE_ID, packageId);
        intent.putExtra(GlobalConstants.EXT_TAG_PACKAGE_IDENTIFIER, packageIdentifier);
        intent.putExtra(GlobalConstants.EXT_TAG_PACKAGE_MEDIA, packageMedia);
        intent.putExtra(GlobalConstants.EXT_TAG_PACKAGE_DURATION, packageDuration);
        intent.putExtra(GlobalConstants.EXT_TAG_PACKAGE_DETAILS, packageDetails);
        context.startActivity(intent);
    }
}
