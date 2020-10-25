package com.example.chcook.KahHeng.EndUser.DA;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chcook.Domain.Videos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class VideoDA {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private ArrayList<Videos> videos = new ArrayList<>();
    private String videokey = "";

    public VideoDA() {
        connectFirebase();
    }

    public VideoDA(String videokey) {
        connectFirebase();
        this.videokey = videokey;
    }

    public String getVideokey() {
        return videokey;
    }

    public void setVideokey(String videokey) {
        this.videokey = videokey;
    }

    public void connectFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public interface VideoCallback {
        ArrayList<Videos> onCallback(ArrayList<Videos> videos);
    }

    public interface Callvideo {
        Videos onCallVideo(Videos video);
    }

    public void getVideoRealData(Callvideo callvideo) {
        getVideoInform(callvideo);
    }
// get All User's Upload View
    public void getUploadedVideo(final VideoCallback videoCallback) {
        DatabaseReference ref = database.getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("video");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        setVideokey(child.getKey());
                        getAllVideos(videoCallback);

                    }

                }else {
                    videoCallback.onCallback(new ArrayList<Videos>());
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getVideoInform(final Callvideo callvideo) {
        final DatabaseReference videoRef = database.getReference().child("Videos").child(videokey);
        videoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Videos video = new Videos();
                if (dataSnapshot.exists()) {
                    Long time = 0L;

                    if (dataSnapshot.exists()) {

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.getKey().equals("URL")) {
                                String url = child.getValue().toString();
                                video.setVideo(url);
                            }
                            if (child.getKey().equals("name")) {
                                String name = child.getValue().toString();
                                video.setName(name);
                            }
                            if (child.getKey().equals("description")) {
                                String desc = child.getValue().toString();
                                video.setDesc(desc);
                            }
                            if (child.getKey().equals("Uploaddate")) {
                                time = Long.valueOf(child.getValue().toString());
                                video.setDate(getDate(time));
                            }
                            if (child.getKey().equals("view")) {
                                int view = 0;
                                view = Integer.valueOf(child.getValue().toString()) + 1;

                                video.setView("" + view + "View");

                                Map<String, Object> addView = new HashMap<>();
                                addView.put("view", view);
                                videoRef.updateChildren(addView);
                            }
                        }
                        callvideo.onCallVideo(video);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
        ;
    }

    public void getAllVideos(final VideoCallback videoCallback){
        DatabaseReference videoRef = database.getReference().child("Videos").child(videokey);
        videoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String url = "";
                    String name = "";
                    Long time = 0L;
                    if (dataSnapshot.exists()) {

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.getKey().equals("URL")) {
                                url = child.getValue().toString();
                            }
                            if (child.getKey().equals("name")) {
                                name = child.getValue().toString();
                            }
                            if (child.getKey().equals("Uploaddate")) {
                                time = Long.valueOf(child.getValue().toString());

                            }
                        }
                        videos.add(new Videos(videokey,name, url, getDate(time)));
                        videoCallback.onCallback(videos);
                    }

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
        ;
    }


    private String getDate(Long timeStamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timeStamp * 1000);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String date = df.format("dd-MM-yyyy hh:mm", cal).toString();
        return date;
    }

    private long getCurrentTimeStamp() {
        long timestamp = System.currentTimeMillis() / 1000;
        return timestamp;
    }


}
