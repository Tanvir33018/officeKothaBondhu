package net.islbd.kothabondhu.document;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.document.Api.MyContent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.MyViewHolder> {
    private ArrayList<MyContent> myContentArrayList;
    private HashMap<String, Integer> hashMap;
    private final int first_position = 0;

    public DocumentAdapter() {
        myContentArrayList = new ArrayList<>();
        hashMap = new HashMap<>();
    }

    public void setMyContentArrayList(ArrayList<MyContent> myContentArrayList) {
        this.myContentArrayList = myContentArrayList;
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.document_message_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.loadContentToViewHolder(position);
    }

    @Override
    public int getItemCount() {
        return myContentArrayList.size();
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView title, body;
        private final View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.messageTitle);
            body = itemView.findViewById(R.id.messageBody);
            view = itemView.findViewById(R.id.messageListView);
        }
        private void loadContentToViewHolder(int position){
            String cat_name = myContentArrayList.get(position).getCat_name();
            String decontent = myContentArrayList.get(position).getDecontent();
            title.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            if(hashMap.get(cat_name) == null){
                hashMap.put(cat_name, position);
            }
            if(hashMap.get(cat_name) == position) {
                title.setText(cat_name);
                title.setVisibility(View.VISIBLE);
                if(position != first_position) view.setVisibility(View.VISIBLE);
            }
            body.setText(decontent);
        }
    }
}
