package net.islbd.kothabondhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.ui.activity.VideoPlayerActivity;

/**
 * Created by UserStatusDetails on 2/27/2019.
 */

public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoRecyclerViewAdapter.ViewHolder> {

    private Context context;

    public VideoRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public VideoRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_video_item, parent, false);
        context = parent.getContext();
        return new VideoRecyclerViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideoRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.playerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        int size = 4;
        /*if (packageList != null) {
            size = packageList.length;
        }*/
        return size;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView titleTextView, descriptionTextView, durationTextView;
        protected ImageView playerImageView;

        ViewHolder(View itemView) {
            super(itemView);
            initializeWidgets(itemView);
        }

        private void initializeWidgets(View itemView) {
            titleTextView = itemView.findViewById(R.id.video_item_title_textView);
            descriptionTextView = itemView.findViewById(R.id.video_item_description_textView);
            durationTextView = itemView.findViewById(R.id.video_item_duration_textView);
            playerImageView = itemView.findViewById(R.id.video_item_player_imageView);
        }
    }
}
