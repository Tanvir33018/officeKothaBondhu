package net.islbd.kothabondhu.ui.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.document.Api.MyContent;
import net.islbd.kothabondhu.document.DocumentAdapter;

import java.util.ArrayList;
import java.util.Comparator;

public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.SelectedViewHolder> {

    private ArrayList<MyContent> newContentArrayList;


    public SelectedAdapter() {
        newContentArrayList = new ArrayList<>();
    }


    public void setNewContentArrayList(ArrayList<MyContent> myContentArrayList) {
        this.newContentArrayList = myContentArrayList;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            myContentArrayList.sort(new Comparator<MyContent>() {
                @Override
                public int compare(MyContent o1, MyContent o2) {
                    if(!o1.getCat_name().equals(o2.getCat_name())) return o1.getCat_name().compareTo(o2.getCat_name());
                    else return o1.getCid().compareTo(o2.getCid());
                }
            });
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SelectedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelectedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.newselecteditem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedViewHolder holder, int position) {
           String catagoryName = newContentArrayList.get(position).getCat_name();
           String titleName = newContentArrayList.get(position).getTitle();
           String bodyText = newContentArrayList.get(position).getDecontent();

           holder.catagoriText.setText(catagoryName);
           holder.titleText.setText(titleName);
           holder.bodyText.setText(bodyText);
    }


    @Override
    public void onViewRecycled(@NonNull SelectedViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return newContentArrayList.size();
    }



    class SelectedViewHolder extends RecyclerView.ViewHolder{

        TextView catagoriText, titleText;
        ExpandableTextView bodyText;

        public SelectedViewHolder(@NonNull View itemView) {
            super(itemView);
            catagoriText = itemView.findViewById(R.id.newMessageCatagory);
            titleText = itemView.findViewById(R.id.newMessageTitle);
            bodyText = itemView.findViewById(R.id.newMessageBody);
        }
    }
}
