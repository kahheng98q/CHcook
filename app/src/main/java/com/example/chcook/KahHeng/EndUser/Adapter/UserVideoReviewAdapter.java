package com.example.chcook.KahHeng.EndUser.Adapter;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chcook.Domain.Videos;
import com.example.chcook.KahHeng.EndUser.DA.RatingDA;
import com.example.chcook.R;

import java.net.URL;
import java.util.ArrayList;
public class UserVideoReviewAdapter  extends RecyclerView.Adapter<UserVideoReviewAdapter.RViewHolder>{
    private ArrayList<Videos> videos;
    private Context context;

    public static class RViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private TextView txtReview;
        private TextView txtView;

        public RViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtReview = itemView.findViewById(R.id.txtReview);
            txtView = itemView.findViewById(R.id.txtView);

        }
    }

    public UserVideoReviewAdapter(Context context, ArrayList<Videos> vd) {
        this.context = context;
        this.videos = vd;
    }
    @NonNull
    @Override
    public UserVideoReviewAdapter.RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_review_cardview, parent, false);
        RViewHolder vh = new RViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserVideoReviewAdapter.RViewHolder holder, int position) {
        final Videos video = videos.get(position);
//        Log.d("test", videos.get(0).getName());
        RatingDA ratingDA=new RatingDA();
        ratingDA.setVideoKey(video.getVideoID());
        ratingDA.getAvgRating((ratings, rating, numofRate) -> {
            holder.txtReview.setText(String.format("%.1f", rating));
        });
//        Toast.makeText(context, video.getUser().getName(), Toast.LENGTH_SHORT).show();
        holder.txtName.setText(video.getName());

        holder.txtView.setText(video.getView());
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }
}
