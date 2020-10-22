package com.example.chcook.DA;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Videos;
import com.example.chcook.KahHeng.EndUser.EditVideo;
import com.example.chcook.KahHeng.EndUser.Pay;
import com.example.chcook.KahHeng.EndUser.PlayVideo;
import com.example.chcook.R;

import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.RViewHolder> {
    private ArrayList<Videos> videos;
    private Context context;
    public static class RViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView textView1;
        private TextView textView2;
        private ConstraintLayout videoLayout;


        public RViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.profilePic);
            textView1=itemView.findViewById(R.id.textViewName);
            textView2=itemView.findViewById(R.id.txtDate);
            videoLayout=itemView.findViewById(R.id.homepageLayout);
        }
    }
    public HistoryAdapter(Context context, ArrayList<Videos> vd){
        this.context=context;
        this.videos=vd;
    }
    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage,parent,false);
        RViewHolder vh=new RViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.RViewHolder holder, int position) {
        final Videos video=videos.get(position);
        if (!video.getVideo().equals("")){
            Glide.with(context)
                    .asBitmap()
                    .load(video.getVideo())
                    .into(holder.imageView);
        }

        holder.textView1.setText(video.getName());
        holder.textView2.setText(video.getDate());
        holder.videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;

                Bundle bundle=new Bundle();
                bundle.putString("key", video.getVideoID());
                //set Fragmentclass Arguments
                PlayVideo fragobj=new PlayVideo();
                fragobj.setArguments(bundle);

                fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.myNavHostFragment,new PlayVideo());
                fragmentTransaction.replace(R.id.myNavHostFragment,fragobj);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }
}
