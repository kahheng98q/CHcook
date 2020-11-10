package com.example.chcook.YangJie.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Videos;
import com.example.chcook.R;
import com.example.chcook.YangJie.GUI.Banning.ShowBanVideo;

import java.util.ArrayList;
import java.util.Locale;


public class recyclerViewAdapter_staffMain extends RecyclerView.Adapter<recyclerViewAdapter_staffMain.ViewHolder>   {
    private ArrayList<Videos> arrayList;
    private ArrayList<Videos> ori;
    private Context mContext;
    private String Vposition,page;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_liststaffmainvideo,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.desc.setText("Desc : "+ori.get(position).getDesc());
        holder.name.setText("Video Name : "+ori.get(position).getName());
        holder.date.setText("Upload Date : "+ori.get(position).getDate());
        holder.cate.setText("Category : "+ori.get(position).getCategory());
        Glide.with(mContext)
                .asBitmap()
                .load(Uri.parse(ori.get(position).getImage()))
                .into(holder.img);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowBanVideo.class);
                intent.putExtra("videoId",ori.get(position).getVideoID());
                intent.putExtra("position",Vposition);
                intent.putExtra("page","staffMain");

                mContext.startActivity(intent);
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
        TextView name,desc,date,cate;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.staffMainImgVideo);
            name = itemView.findViewById(R.id.staffMainVideoName);
            desc = itemView.findViewById(R.id.staffMainVideoDesc);
            date = itemView.findViewById(R.id.staffMainDate);
            cate = itemView.findViewById(R.id.txtCategoryMain);
            parentLayout = itemView.findViewById(R.id.staffMain_parentLayout);
        }
    }

;

    public recyclerViewAdapter_staffMain(Context mContext,ArrayList<Videos> videos,String position) {
        this.ori = videos;
        this.Vposition = position;
        arrayList = new ArrayList<>();
        this.arrayList.addAll(videos);
        this.mContext = mContext;
    }

    public void filter(String characterText) {
        String input = characterText.toLowerCase(Locale.getDefault());
        ori.clear();
        if (input.length() == 0) {
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
    public void category(String characterText) {
        String input = characterText.toLowerCase(Locale.getDefault());
        ori.clear();
        if (input.equals("all")) {
            ori.addAll(arrayList);
        } else {
            ori.clear();
            for (Videos item: arrayList) {
                if (item.getCategory().contains(characterText)) {
                    ori.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

}
