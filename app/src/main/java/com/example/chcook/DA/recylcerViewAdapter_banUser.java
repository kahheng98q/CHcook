package com.example.chcook.DA;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Report;
import com.example.chcook.Domain.User;
import com.example.chcook.R;
import com.example.chcook.YangJie.ShowBanUser;
import com.example.chcook.YangJie.ShowBanVideo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class recylcerViewAdapter_banUser extends RecyclerView.Adapter<recylcerViewAdapter_banUser.viewHolder> {

    private ArrayList<Report> banUser = new ArrayList<>();
    private Context mContext;
    private String uId, vName, uName, uStatus;

    public recylcerViewAdapter_banUser(Context mContext, ArrayList<Report> banUser) {
        this.banUser = banUser;
        this.mContext = mContext;
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
                    uId = dataSnapshot.child("userId").getValue(String.class);
                    vName = dataSnapshot.child("name").getValue(String.class);
                    holder.videoName.setText("video name: " + vName);


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
                                uName = dataSnapshot.child("Name").getValue(String.class);
                                uStatus = dataSnapshot.child("Banned").getValue(String.class);
                                holder.name.setText("UserName :"+uName+" - "+uStatus);
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


        holder.reporter.setText("reported by " + banUser.get(position).getUsername());


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowBanUser.class);
                intent.putExtra("videoId",banUser.get(position).getVideoId());
                intent.putExtra("reportId",banUser.get(position).getReportId());
                intent.putExtra("userId",uId);
                mContext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return banUser.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        CircleImageView banUserImg;
        TextView name, videoName, reporter;
        RelativeLayout parentLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            banUserImg = itemView.findViewById(R.id.banUserProfile);
            name = itemView.findViewById(R.id.banUserName);
            videoName = itemView.findViewById(R.id.banUserVideoName);
            reporter = itemView.findViewById(R.id.banUserReporter);
            parentLayout = itemView.findViewById(R.id.banUserParent_Layout);
        }
    }
}
