package com.example.chcook.KahHeng.EndUser.DA;

import androidx.annotation.NonNull;

import com.example.chcook.KahHeng.EndUser.Domain.Videos;
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
    private ArrayList<Videos> videos=new ArrayList<>();
private String videokey="";
    public VideoDA(){
        connectFirebase();
    }
    public VideoDA(String videokey){
        connectFirebase();
        this.videokey=videokey;
    }

    public void connectFirebase(){
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }
    public interface Callback{
        ArrayList<Videos> onCallback(ArrayList<Videos> videos);
    }
    public interface Callvideo{
      Videos onCallVideo(Videos video);
    }
    public void getVideoRealData(Callvideo callvideo){
        getVideoInform(callvideo);
    }
    public void getVideoInform(final Callvideo callvideo) {
        final DatabaseReference videoRef = database.getReference().child("Videos").child(videokey);
        videoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Videos video=new Videos();
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
