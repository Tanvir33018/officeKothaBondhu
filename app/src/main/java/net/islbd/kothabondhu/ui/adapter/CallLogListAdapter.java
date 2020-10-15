package net.islbd.kothabondhu.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.model.pojo.CallHistoryDetails;


/**
 * Created by wahid.sadique on 9/14/2017.
 */

public class CallLogListAdapter extends RecyclerView.Adapter<CallLogListAdapter.ViewHolder> {
    private Context context;
    private List<CallHistoryDetails> callHistoryDetailsList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_call_log_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int currentPosition = position;
        final String name = callHistoryDetailsList.get(position).getAgentName();
        final String callDate = callHistoryDetailsList.get(position).getCallDate();
        final String duration = callHistoryDetailsList.get(position).getDuration();

        if (name != null) {
            holder.nameTextView.setText(name);
        }
        if (callDate != null) {
            holder.dateTextView.setText(callDate);
        }
        if (duration != null) {
            holder.durationTextView.setText(duration);
        }
    }

    @Override
    public int getItemCount() {
        if (callHistoryDetailsList == null) {
            return 0;
        }
        return callHistoryDetailsList.size();
    }

    public List<CallHistoryDetails> getCallHistoryDetailsList() {
        return callHistoryDetailsList;
    }

    public void setCallHistoryDetailsList(List<CallHistoryDetails> callHistoryDetailsList) {
        this.callHistoryDetailsList = callHistoryDetailsList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView nameTextView, durationTextView, dateTextView;


        ViewHolder(View itemView) {
            super(itemView);
            initializeWidgets(itemView);
        }

        private void initializeWidgets(View itemView) {
            nameTextView = itemView.findViewById(R.id.call_log_item_name_textView);
            durationTextView = itemView.findViewById(R.id.call_log_item_duration_textView);
            dateTextView = itemView.findViewById(R.id.call_log_item_date_textView);
        }
    }
}
