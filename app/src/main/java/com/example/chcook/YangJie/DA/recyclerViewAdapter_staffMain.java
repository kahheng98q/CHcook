package com.example.chcook.YangJie.DA;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Videos;
import com.example.chcook.R;
import com.example.chcook.YangJie.MainFragment_staff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;


public class recyclerViewAdapter_staffMain extends RecyclerView.Adapter<recyclerViewAdapter_staffMain.ViewHolder>   {
    private ArrayList<Videos> arrayList;
    private ArrayList<Videos> ori;
    private Context mContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_liststaffmainvideo,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.desc.setText(ori.get(position).getDesc());
        holder.name.setText(ori.get(position).getName());
//        holder.name.setText(ori.get(position));
        Glide.with(mContext)
                .asBitmap()
                .load(Uri.parse(ori.get(position).getImage()))
                .into(holder.img);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,ori.get(position).getName(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ori.size();
    }

//    @Override
//    public Filter getFilter() {
//        return arrayListFilter;
//    }
//    private Filter arrayListFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            ArrayList<Videos> filteredList = new ArrayList<>();
//            if(constraint==null||constraint.length()==0){
//                filteredList.addAll(arrayList);
//            }else{
//                String filterPattern = constraint.toString().toLowerCase().trim();
//
//                for(Videos item:arrayList){
//                    if(item.getName().toLowerCase().contains(filterPattern)){
//                        filteredList.add(item);
//                    }
//                }
//            }
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            ori.clear();
//            ori.addAll((ArrayList)results.values);
//            notifyDataSetChanged();
//        }
//    };


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name,desc,date;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.staffMainImgVideo);
            name = itemView.findViewById(R.id.staffMainVideoName);
            desc = itemView.findViewById(R.id.staffMainVideoDesc);
            date = itemView.findViewById(R.id.staffMainDate);
            parentLayout = itemView.findViewById(R.id.staffMain_parentLayout);
        }
    }

;

    public recyclerViewAdapter_staffMain(Context mContext,ArrayList<Videos> videos) {
        this.ori = videos;
//        arrayList = new ArrayList<>(videos);
        arrayList = new ArrayList<>();
        this.arrayList.addAll(videos);
        this.mContext = mContext;
    }

    public void filter(String characterText) {
        String input = characterText.toLowerCase(Locale.getDefault());
        ori.clear();
        if (input.length() == 0) {
            Toast.makeText(mContext,"empty",Toast.LENGTH_SHORT).show();
            ori.addAll(arrayList);
        } else {
            ori.clear();
            for (Videos item: arrayList) {
                if (item.getName().toLowerCase(Locale.getDefault()).contains(characterText)) {
                    ori.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }


}
