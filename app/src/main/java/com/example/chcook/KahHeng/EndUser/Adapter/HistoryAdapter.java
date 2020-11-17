package com.example.chcook.KahHeng.EndUser.Adapter;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Histories;

import com.example.chcook.KahHeng.EndUser.DA.HistoryDA;
import com.example.chcook.KahHeng.EndUser.GUI.VideoPlaying.PlayVideo;
import com.example.chcook.R;

import java.util.ArrayList;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.RViewHolder> implements PopupMenu.OnMenuItemClickListener {
    private ArrayList<Histories> histories;
    private Context context;
    private HistoryDA historyDA = new HistoryDA();

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

    public HistoryAdapter(Context context, ArrayList<Histories> his) {
        this.context = context;
        this.histories =reverselIndex( his);

//        Log.d("test","GGGGGGGGGGGGGGGGGGGGGGGGG");
    }

    private ArrayList<Histories> reverselIndex(ArrayList<Histories> his) {
        ArrayList<Histories> tmphis = new ArrayList<>();

        for (int i = his.size(); i >0; i--) {
            tmphis.add(his.get(i-1));
        }
        return tmphis;
    }

    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewcontent, parent, false);
        RViewHolder vh = new RViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.RViewHolder holder, final int position) {

        final Histories history = histories.get(position);
        if (!history.getVideos().getVideo().equals("")) {
            Glide.with(context)
                    .asBitmap()
                    .load(history.getVideos().getVideo())
                    .into(holder.imageView);
        }

        holder.textView1.setText(history.getVideos().getName());
        holder.textView2.setText(history.getHistoryDate());
        holder.videoInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;

                Bundle bundle = new Bundle();
                bundle.putString("key", history.getVideos().getVideoID());
                //set Fragmentclass Arguments
                PlayVideo fragobj = new PlayVideo();
                fragobj.setArguments(bundle);

                fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.myNavHostFragment, fragobj);
                fragmentTransaction.addToBackStack(null).commit();
            }
        });
        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "Removed Video in Histories", Toast.LENGTH_SHORT).show();
                PopupMenu popupMenu = new PopupMenu(context, v);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.itemHDelete:
                                historyDA.deletehis(histories.get(position).getHistoryId());
                                Toast.makeText(context, "Removed Video in Histories", Toast.LENGTH_SHORT).show();
                                histories.remove(position);
                                notifyDataSetChanged();
                                return true;

                            default:
                                return false;
                        }

                    }
                });
                popupMenu.inflate(R.menu.his_menu);
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }
}
