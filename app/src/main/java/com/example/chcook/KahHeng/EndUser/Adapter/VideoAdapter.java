package com.example.chcook.KahHeng.EndUser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Videos;

import com.example.chcook.KahHeng.EndUser.GUI.VideoAndRecipeManagement.EditVideoInfo;
import com.example.chcook.KahHeng.EndUser.GUI.VideoAndRecipeManagement.EditVideoInformation;
import com.example.chcook.KahHeng.EndUser.GUI.VideoPlaying.PlayVideo;
import com.example.chcook.R;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.RViewHolder> implements MenuItem.OnMenuItemClickListener {
    private ArrayList<Videos> videos;
    private Context context;

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    public static class RViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView1;
        private TextView textView2;
        private LinearLayout videoInfoLayout;
        private ImageButton btnMore;

        public RViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profilePic);
            textView1 = itemView.findViewById(R.id.textViewName);
            textView2 = itemView.findViewById(R.id.textViewAddress);
            btnMore = itemView.findViewById(R.id.btnMultiFunc);
            videoInfoLayout = itemView.findViewById(R.id.videoinfoLayout);

        }
    }

    public VideoAdapter(Context context, ArrayList<Videos> vd) {
        this.context = context;
        this.videos = vd;

    }

    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewcontent, parent, false);
        RViewHolder vh = new RViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RViewHolder holder, final int position) {
        final Videos video = videos.get(position);
        if (!video.getVideo().isEmpty() || !video.getVideo().equals("")) {
            Glide.with(context)
                    .asBitmap()
                    .load(video.getVideo())
                    .into(holder.imageView);
        }
//        Toast.makeText(context, videos.get(1).getVideoID(), Toast.LENGTH_SHORT).show();
        holder.textView1.setText(video.getName());
        holder.textView2.setText(video.getDate());
        holder.videoInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, videos.get(0).getVideoID(), Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;

                Bundle bundle = new Bundle();
                bundle.putString("key", video.getVideoID());
                //set Fragmentclass Arguments
                PlayVideo fragobj = new PlayVideo();
                fragobj.setArguments(bundle);

                fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.myNavHostFragment,new PlayVideo());
                fragmentTransaction.replace(R.id.myNavHostFragment, fragobj);
                fragmentTransaction.addToBackStack(null).commit();

//                Toast.makeText(context, "Added Video in Favorite List", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.itemVDelete:
                                return true;
                            case R.id.itemVEdit:
                                FragmentManager fragmentManager;
                                FragmentTransaction fragmentTransaction;
                                Bundle bundle = new Bundle();
                                bundle.putString("key", video.getVideoID());
                                //set Fragmentclass Arguments
                                EditVideoInformation fragobj = new EditVideoInformation();
                                fragobj.setArguments(bundle);
                                fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.myNavHostFragment, fragobj);
                                fragmentTransaction.addToBackStack(null).commit();
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.video_manage_menu);
                popupMenu.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }


}
