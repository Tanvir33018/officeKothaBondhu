package net.islbd.kothabondhu.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import net.islbd.kothabondhu.MockData;
import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.event.IPackageSelectListener;
import net.islbd.kothabondhu.model.pojo.AgentDetails;
import net.islbd.kothabondhu.presenter.IDbInteractor;
import net.islbd.kothabondhu.ui.activity.AgentProfileActivity;
import net.islbd.kothabondhu.utility.SharedPrefUtils;

/**
 * Created by wahid.sadique on 9/17/2017.
 */

public class AgentListAdapter extends RecyclerView.Adapter<AgentListAdapter.ViewHolder> {
    private Context context;
    private List<AgentDetails> agentList;
    private IDbInteractor dbInteractor;
    private int selectedPackageIndex;
    private SharedPreferences sharedPref;
    private IPackageSelectListener packageSelectListener;
    private Activity activity;

    public static final String PHOTO_URL_TAG = "_PHOTO";
    public static final String NAME_TAG = "_NAME";
    public static final String LOCATION_TAG = "_LOCATION";
    public static final String AGE_TAG = "_AGE";
    public static final String STATUS_TAG = "_STATUS";
    public static final String ID_TAG = "_ID";

    public AgentListAdapter(Context context, IPackageSelectListener packageSelectListener, IDbInteractor dbInteractor, SharedPreferences sharedPref) {
        this.context = context;
        this.dbInteractor = dbInteractor;
        this.sharedPref = sharedPref;
        this.packageSelectListener = packageSelectListener;
    }

    @NonNull
    @Override
    public AgentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_agent_item, parent, false);
        return new AgentListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AgentListAdapter.ViewHolder holder, int position) {
        final String id = agentList.get(position).getAgentId();
        final String name = agentList.get(position).getAgentName();
        final String location = "Dhaka"; // TODO: ask for location in api
        final String url = agentList.get(position).getAgentProfilePic();
        final String age = agentList.get(position).getAgentAge() + " years";
        final String status = agentList.get(position).getOnlineStatus();
        //final String url = "https://i.ytimg.com/vi/TZiQK81Rjfw/maxresdefault.jpg";

        final boolean isFavourite = dbInteractor.checkCallLog(id);

        //holder.agentImageView.setImageBitmap(getMarkerBitmap(isFavourite));
        //holder.agentImageView.setImageBitmap(ImageShaper.getRoundedCornerImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.brd_pit), 100));
        //holder.agentImageView.getLayoutParams().height = new Random().nextInt(300) + 250;

        if (status.equals("1")) {
            holder.statusImageView.setVisibility(View.VISIBLE);
        } else {
            holder.statusImageView.setVisibility(View.INVISIBLE);
        }

        loadImage(url, holder.agentImageView, holder.iconProgressBar);
        holder.nameTextView.setText(name);
        holder.ageTextView.setText(age);
        holder.agentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.agentImageView.setBorderColor(context.getResources().getColor(R.color.green_400));
                if (id != null && url != null && age != null && name != null && status != null) {
                    Intent intent = new Intent(context, AgentProfileActivity.class);
                    intent.putExtra(PHOTO_URL_TAG, url);
                    intent.putExtra(NAME_TAG, name);
                    intent.putExtra(AGE_TAG, age);
                    intent.putExtra(STATUS_TAG, status);
                    intent.putExtra(LOCATION_TAG, location);
                    intent.putExtra(ID_TAG, id);
                    context.startActivity(intent);
                }
            }
        });

        holder.callImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPref.edit().putInt(SharedPrefUtils._TO_USER_PHONE, Integer.valueOf(id)).apply();
                packageSelectListener.onPackageSelection(id, url);
                //showPackageDialog(id, name, url);
            }
        });

        holder.chatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        /*holder.itemConsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (url != null) {
                    Intent intent = new Intent(context, NewsActivity.class);
                    intent.putExtra(GlobalConstants.EXT_TAG_URL, url);
                    intent.putExtra(GlobalConstants.EXT_TAG_NAME, name);
                    context.startActivity(intent);
                }

            }
        });*/
    }

    private void showPackageDialog(final String id, final String name, final String url) {
        selectedPackageIndex = -1;
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog);
        alertDialog.setTitle(context.getString(R.string.package_dialog_title));
        alertDialog.setSingleChoiceItems(new MockData().getPackageArray(), selectedPackageIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedPackageIndex = i;
            }
        });

        alertDialog.setPositiveButton(context.getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sharedPref.edit().putInt(SharedPrefUtils._TO_USER_PHONE, Integer.valueOf(id)).apply();
                dbInteractor.markCall(id, name, 0);
                /*Intent intent = new Intent(context, CallActivity.class);
                context.startActivity(intent);*/
                //Toast.makeText(context, String.valueOf(selectedPackageIndex), Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(context, SinchCallActivity.class);
                intent.putExtra(SinchCallActivity.EXTRA_RECIPIENT_ID, id);*/
                packageSelectListener.onPackageSelection(id, url);
            }
        });

        alertDialog.setNegativeButton(context.getString(R.string.dialog_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, context.getString(R.string.package_dialog_toast), Toast.LENGTH_SHORT).show();
            }
        });


        alertDialog.show();
    }

    private Bitmap getMarkerBitmap(Boolean isFavourite) {
        if (isFavourite) {
            return BitmapFactory.decodeResource(context.getResources(), android.R.drawable.star_on);
        } else {
            return BitmapFactory.decodeResource(context.getResources(), android.R.drawable.star_off);
        }
    }

    public List<AgentDetails> getAgentList() {
        return agentList;
    }

    public void setAgentList(List<AgentDetails> agentList) {
        this.agentList = agentList;
    }

    private void loadImage(String url, ImageView imageView, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        Picasso picasso = Picasso.get();
        //picasso.setDebugging(true);
        picasso.load(url).error(R.drawable.ic_person).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (agentList == null || agentList.isEmpty()) {
            return 0;
        }

        return agentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView nameTextView, ageTextView, locationTextView;
        protected CircularImageView agentImageView;
        protected ImageView callImageView, chatImageView, statusImageView;
        protected ProgressBar iconProgressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            initializeWidgets(itemView);
        }

        private void initializeWidgets(View itemView) {
            nameTextView = itemView.findViewById(R.id.agent_item_name_textView);
            agentImageView = (CircularImageView) itemView.findViewById(R.id.agent_item_user_imageView);
            callImageView = itemView.findViewById(R.id.agent_item_call_imageView);
            chatImageView = itemView.findViewById(R.id.agent_item_chat_imageView);
            iconProgressBar = itemView.findViewById(R.id.item_agent_icon_progress_bar);
            ageTextView = itemView.findViewById(R.id.agent_item_age_textView);
            locationTextView = itemView.findViewById(R.id.agent_item_location_textView);
            statusImageView = itemView.findViewById(R.id.agent_item_user_status_imageView);
        }
    }
}
