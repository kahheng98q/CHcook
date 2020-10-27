package com.example.chcook.KahHeng.EndUser.DA;
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
import com.example.chcook.Domain.Favorite;
import com.example.chcook.KahHeng.EndUser.PlayVideo;
import com.example.chcook.R;

import java.util.ArrayList;
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.RViewHolder> implements PopupMenu.OnMenuItemClickListener
{
    private ArrayList<Favorite> favorites;
    private Context context;
    private FavoriteDA favoriteDA = new FavoriteDA();

    public static class RViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView1;
        private TextView textView2;
        private LinearLayout videoInfoLayout;
        private ImageButton btnMore;


        public RViewHolder(@NonNull View itemView) {
            super(itemView);
//            imageView=itemView.findViewById(R.id.profilePic);
//            textView1=itemView.findViewById(R.id.textViewName);
//            textView2=itemView.findViewById(R.id.txtDate);
//            videoLayout=itemView.findViewById(R.id.homepageLayout);
//
            imageView = itemView.findViewById(R.id.profilePic);
            textView1 = itemView.findViewById(R.id.textViewName);
            textView2 = itemView.findViewById(R.id.textViewAddress);
            btnMore = itemView.findViewById(R.id.btnMultiFunc);
            videoInfoLayout = itemView.findViewById(R.id.videoinfoLayout);
        }

    }

    public FavoriteAdapter(Context context, ArrayList<Favorite> favorites) {
        this.context = context;
        this.favorites = favorites;
//        Log.d("test","GGGGGGGGGGGGGGGGGGGGGGGGG");
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewcontent, parent, false);
        RViewHolder vh = new RViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RViewHolder holder, final int position) {
        final Favorite favorite = favorites.get(position);
        if (!favorite.getVideos().getVideo().equals("")) {
            Glide.with(context)
                    .asBitmap()
                    .load(favorite.getVideos().getVideo())
                    .into(holder.imageView);
        }

        holder.textView1.setText(favorite.getVideos().getName());
        holder.textView2.setText(favorite.getFavoriteDate());
        holder.videoInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;

                Bundle bundle = new Bundle();
                bundle.putString("key", favorite.getVideos().getVideoID());
                //set Fragmentclass Arguments
                PlayVideo fragobj = new PlayVideo();
                fragobj.setArguments(bundle);

                fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.myNavHostFragment, fragobj);
                fragmentTransaction.commit();
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
                            case R.id.itemFDelete:
                                favoriteDA.setFavoriteKey(favorites.get(position).getFavoriteId());
                                favoriteDA.deleteFav();
                                Toast.makeText(context, "Removed Video in Favorite List", Toast.LENGTH_SHORT).show();
                                favorites.remove(position);

                                notifyDataSetChanged();
                                return true;
                            default:
                                return false;
                        }

                    }
                });
                popupMenu.inflate(R.menu.fav_menu);
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }
}
