package com.example.chcook.DA;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Report;
import com.example.chcook.R;
import com.example.chcook.YangJie.ShowBanVideo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class recyclerViewAdapter_banVideo extends RecyclerView.Adapter<recyclerViewAdapter_banVideo.viewHolder> {
    private static final String TAG = "recyclerViewAdapter_ban";
    private ArrayList<Report> banVideo = new ArrayList<>();
    private String VID,VName,VDesc,VReporter,VDate;

    private Context mContext;

    public class viewHolder extends RecyclerView.ViewHolder {

        RelativeLayout parentLayout;
        ImageView vImg;
        TextView txtVideoName, txtDate, reporterName, videoStatus, reason;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txtVideoName = itemView.findViewById(R.id.banVideoTxtVideoName);
            txtDate = itemView.findViewById(R.id.banVideoTxtVideoUploadTime);
            reporterName = itemView.findViewById(R.id.banVideoTxtReporter);
            videoStatus = itemView.findViewById(R.id.banVideoTxtVideoStatus);
            reason = itemView.findViewById(R.id.banVideoTxtReason);
            vImg = itemView.findViewById(R.id.banVideoImgCover);
            parentLayout = itemView.findViewById(R.id.banVideoParent_Layout);


        }
    }

    public recyclerViewAdapter_banVideo(Context context,ArrayList<Report> rp) {
//        this.bVideoName = videoName;
        this.banVideo = rp;
        this.mContext = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listbanvideo,parent,false);
        viewHolder holder = new viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: videoCalledtoBanvideo");
        final Report v = banVideo.get(position);
        holder.txtVideoName.setText("Video Name:"+v.getVideoName());
        holder.txtDate.setText(v.getDate());
        holder.reason.setText(v.getReason());
        holder.reporterName.setText(v.getUsername());
        holder.videoStatus.setText("approval");
        VID = v.getVideoId();
        VName=v.getVideoName();
        VDesc=v.getReason();
        VReporter=v.getUsername();
        VDate = v.getDate();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Videos");
        DatabaseReference df = databaseReference.child(v.getVideoId());
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String videoUrl = dataSnapshot.child("URL").getValue(String.class);
                    Glide.with(mContext)
                            .asBitmap()
                            .load(Uri.parse(videoUrl))
                            .into(holder.vImg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowBanVideo.class);
                intent.putExtra("videoId",VID);
                intent.putExtra("description",VDesc);
                intent.putExtra("videoName",VName);
                intent.putExtra("reporter",VReporter);
                intent.putExtra("date",VDate);
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return banVideo.size();
    }


}
