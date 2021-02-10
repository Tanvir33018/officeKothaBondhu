package net.islbd.kothabondhu.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.model.pojo.PackageHistoryDetails;

import java.util.List;

public class PackageHistoryListAdapter extends RecyclerView.Adapter<PackageHistoryListAdapter.ViewHolder> {
    private Context context;
    private List<PackageHistoryDetails> packageHistoryDetailsList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_package_history_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int currentPosition = position;
        final String packDetails = packageHistoryDetailsList.get(position).getPackDetails();
        final String packDuration = packageHistoryDetailsList.get(position).getPackDuration();
        final String packExpireDate = packageHistoryDetailsList.get(position).getPackExpireDate();

        if (packDetails != null) {
            holder.packageDetailsTextView.setText(packDetails);
        }
        if (packDuration != null) {
            holder.packageDurationTextView.setText(packDuration +" munites");
        }
        if (packExpireDate != null) {
            holder.packageExpireTextView.setText(packExpireDate);
        }
    }

    @Override
    public int getItemCount() {
        if (packageHistoryDetailsList == null) {
            return 0;
        }
        return packageHistoryDetailsList.size();
    }

    public List<PackageHistoryDetails> getPackageHistoryDetailsList() {
        return packageHistoryDetailsList;
    }

    public void setPackageHistoryDetailsList(List<PackageHistoryDetails> packageHistoryDetailsList) {
        this.packageHistoryDetailsList = packageHistoryDetailsList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView packageDetailsTextView, packageExpireTextView, packageDurationTextView;


        ViewHolder(View itemView) {
            super(itemView);
            initializeWidgets(itemView);
        }

        private void initializeWidgets(View itemView) {
            packageDetailsTextView = itemView.findViewById(R.id.package_details_textView);
            packageExpireTextView = itemView.findViewById(R.id.package_expire_textView);
            packageDurationTextView = itemView.findViewById(R.id.package_duration_textView);
        }
    }
}
