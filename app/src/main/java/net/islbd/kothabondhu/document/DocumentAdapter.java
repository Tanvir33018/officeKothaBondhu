package net.islbd.kothabondhu.document;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.document.Api.MyContent;

import java.util.ArrayList;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.MyViewHolder> {
    private ArrayList<MyContent> myContentArrayList;

    public DocumentAdapter() {
        myContentArrayList = new ArrayList<>();
    }

    public void setMyContentArrayList(ArrayList<MyContent> myContentArrayList) {
        this.myContentArrayList = myContentArrayList;
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
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.messageTitle);
            body = itemView.findViewById(R.id.messageBody);
        }
        private void loadContentToViewHolder(int position){
            String cat_name = myContentArrayList.get(position).getCat_name();
            String decontent = myContentArrayList.get(position).getDecontent();
            title.setText(cat_name);
            body.setText(decontent);
        }
    }
}
