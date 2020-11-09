package com.example.chcook.YangJie.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Report;
import com.example.chcook.R;
import com.example.chcook.YangJie.GUI.Banning.ShowBanUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class recylcerViewAdapter_banUser extends RecyclerView.Adapter<recylcerViewAdapter_banUser.viewHolder> {

    private ArrayList<Report> banUser = new ArrayList<>();
    private ArrayList<String> userId = new ArrayList<>();
    private Context mContext;
    private String uId, vName, uName, uStatus,VPosition;

    public recylcerViewAdapter_banUser(Context mContext, ArrayList<Report> banUser,String position) {
        this.banUser = banUser;
        this.mContext = mContext;
        this.VPosition = position;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listbanuser, parent, false);
        viewHolder holder = new viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {

        DatabaseReference databaseReferenceV = FirebaseDatabase.getInstance().getReference("Videos");

        databaseReferenceV.child(banUser.get(position).getVideoId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    vName = dataSnapshot.child("name").getValue(String.class);
                    holder.videoName.setText("Reported video : " + vName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference databaseReferenceU = FirebaseDatabase.getInstance().getReference("Users");

        databaseReferenceU.orderByChild("video").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals("video")) {
                        Map<String, Boolean> vMap = (Map<String, Boolean>) child.getValue();
                        for (String userVideoKey : vMap.keySet()) {
                            if (userVideoKey.equals(banUser.get(position).getVideoId())) {
                                String uri = dataSnapshot.child("Image").getValue(String.class);
                                Glide.with(mContext)
                                        .asBitmap()
                                        .load(Uri.parse(uri))
                                        .error(R.drawable.load_error_24dp)
                                        .into(holder.banUserImg);
                                uId = dataSnapshot.getKey();
//                                setUserId(dataSnapshot.getKey());
                                userId.add(uId);
                                uName = dataSnapshot.child("Name").getValue(String.class);

                                if(dataSnapshot.hasChild("Status")){
                                    uStatus = dataSnapshot.child("Status").child("Status").getValue(String.class);
                                }else{
                                    uStatus = "Approval";
                                }
                                String st;
                                if(uStatus.equals("Banned")){
                                    holder.sttus.setText("Status : "+uStatus);
                                    holder.sttus.setTextColor(Color.parseColor("#FF0000"));

                                }else{
                                    holder.sttus.setText("Status : "+uStatus);
                                }

                                holder.name.setText("UserName : "+uName);
                            }
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowBanUser.class);
                intent.putExtra("videoId",banUser.get(position).getVideoId());
                intent.putExtra("reportId",banUser.get(position).getReportId());
                intent.putExtra("userId",userId.get(position));
                intent.putExtra("position",VPosition);
                mContext.startActivity(intent);

            }
        });


    }
    private String getUserId(){
        return uId;
    }
    private void setUserId(String uId) {
        this.uId=uId;
    }

    @Override
    public int getItemCount() {
        return banUser.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        CircleImageView banUserImg;
        TextView name, videoName, sttus;
        RelativeLayout parentLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            banUserImg = itemView.findViewById(R.id.banUserProfile);
            name = itemView.findViewById(R.id.banUserName);
            videoName = itemView.findViewById(R.id.banUserVideoName);
            sttus = itemView.findViewById(R.id.banUserState);
            parentLayout = itemView.findViewById(R.id.banUserParent_Layout);
        }
    }
}
