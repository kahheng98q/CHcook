package com.example.chcook.DA;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Videos;
import com.example.chcook.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter <Adapter.RViewHolder>{
    private ArrayList<Videos> videos;
    private Context context;
    public static class RViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView textView1;
        private TextView textView2;

        public RViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.profilePic);
            textView1=itemView.findViewById(R.id.textViewName);
            textView2=itemView.findViewById(R.id.textViewAddress);
        }
    }
    public Adapter(Context context,ArrayList<Videos> vd){
            this.context=context;
            this.videos=vd;
    }
    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewcontent,parent,false);
        RViewHolder vh=new RViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RViewHolder holder, int position) {
        Videos video=videos.get(position);
        if (!video.getImage().equals("")){
            Glide.with(context)
                    .asBitmap()
                    .load(video.getImage())
                    .into(holder.imageView);
        }

        holder.textView1.setText(video.getName());
        holder.textView2.setText(video.getDate());
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }
}