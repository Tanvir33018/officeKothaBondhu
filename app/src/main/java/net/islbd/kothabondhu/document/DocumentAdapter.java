package net.islbd.kothabondhu.document;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
//import com.softbd.aamarpay.model.RequiredFields;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.document.Api.MyContent;
import net.islbd.kothabondhu.document.docfragment.SelectedFragment;
import net.islbd.kothabondhu.ui.activity.HomeTabActivity;
import net.islbd.kothabondhu.ui.fragment.NewSelectedItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.MyViewHolder> {
    private ArrayList<MyContent> myContentArrayList;
    private HashMap<String, Integer> hashMap;
    private final int first_position = 0;
    public Context context;
    public Fragment fragment;

    public DocumentAdapter() {
        myContentArrayList = new ArrayList<>();
        hashMap = new HashMap<>();
        fragment = new NewSelectedItem();
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
        if(context == null){
            context=parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.document_message_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.loadContentToViewHolder(position);
        //holder.parent.setBackgroundColor(Color.parseColor("#00CC66"));
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
        private final TextView catagory, title;
        private final TextView body;
        public RelativeLayout parent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            catagory = itemView.findViewById(R.id.messageCatagory);
            body = itemView.findViewById(R.id.messageBody);
            title = itemView.findViewById(R.id.messageTitle);
            parent = itemView.findViewById(R.id.parentLayout2);
        }
        private void loadContentToViewHolder(int position){
            String cat_name = myContentArrayList.get(position).getCat_name();
            String decontent = myContentArrayList.get(position).getDecontent();
            String titleString = myContentArrayList.get(position).getTitle();


            catagory.setText(cat_name);
            title.setText(titleString);
            body.setText(decontent);
            String getName = myContentArrayList.get(position).getCat_name();
            String getTitle = myContentArrayList.get(position).getTitle();
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("Catagory",myContentArrayList.get(position).getCat_name());
                    bundle.putString("Title",myContentArrayList.get(position).getTitle());
                    bundle.putString("Content",myContentArrayList.get(position).getDecontent());
                    NewSelectedItem newSelectedItem = new NewSelectedItem();
                    newSelectedItem.setArguments(bundle);
                    ((DocumentActivity)context).getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                            .replace(R.id.fragmentContainerDocument, newSelectedItem)
                            .commit();
                }
            });
        }
    }

}
