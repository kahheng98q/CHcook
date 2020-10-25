package com.example.chcook.KahHeng.EndUser.DA;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Videos;

import com.example.chcook.KahHeng.EndUser.EditVideoInfo;
import com.example.chcook.KahHeng.EndUser.PlayVideo;
import com.example.chcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.RViewHolder> implements MenuItem.OnMenuItemClickListener {
    private ArrayList<Videos> videos;
    private Context context;
    private String type;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private ArrayList<String> adaptIds;
    private HistoryDA historyDA;

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

    public Adapter(Context context, ArrayList<Videos> vd, String type, ArrayList<String> adaptIds) {
        this.context = context;
        this.videos = vd;
        this.type = type;
        this.adaptIds = adaptIds;
        this.historyDA = new HistoryDA();
        setInstance();
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

        holder.textView1.setText(video.getName());
        holder.textView2.setText(video.getDate());
        holder.videoInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                fragmentTransaction.commit();

//                Toast.makeText(context, "Added Video in Favorite List", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                switch (type) {
                    case "video":
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.itemVDelete:

                                        return true;
                                    case R.id.itemVEdit:
                                        Intent intent = new Intent(context, EditVideoInfo.class);
                                        intent.putExtra("Key", video.getVideoID());
                                        context.startActivity(intent);
//                                        Toast.makeText(context, video.getVideoID(), Toast.LENGTH_SHORT).show();

//
                                        return true;

                                }
                                return false;
                            }
                        });
                        popupMenu.inflate(R.menu.video_manage_menu);
                        break;
                    case "favorite":
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.itemFDelete:
                                        String favkey = adaptIds.get(position);
                                        deletefav(favkey);
                                        return true;
                                    default:
                                        return false;
                                }

                            }
                        });
                        popupMenu.inflate(R.menu.fav_menu);
                        break;
                }
                popupMenu.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    private void setInstance() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    private void addfav(String videokey) {

    }

    private void deletefav(String favkey) {
        if (!favkey.isEmpty() || !favkey.equals("")) {
//            Log.d("test", "message text:" + favkey);
            String uid = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference favRef = database.getReference("Favorite").child(favkey);
            DatabaseReference userFavRef = database.getReference("Users").child(uid).child("Favorite").child(favkey);
            favRef.removeValue();
            userFavRef.removeValue();
            Toast.makeText(context, "Removed Video in Favorite List", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("test", "message text:");
        }
    }


    private void deletevideo(String videokey) {


    }
}
