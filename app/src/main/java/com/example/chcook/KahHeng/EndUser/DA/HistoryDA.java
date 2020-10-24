package com.example.chcook.KahHeng.EndUser.DA;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chcook.KahHeng.EndUser.Domain.Histories;
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

public class HistoryDA {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private ArrayList<Histories> histories;
//    private ArrayList<Videos> videos;

    public HistoryDA() {
        connectFirebase();
        histories=new ArrayList<>();
    }
    public void connectFirebase(){
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public interface HisCallback{
        ArrayList<Histories> onCallback(ArrayList<Histories> histories);
    }

    public void getHistoryRealData(HisCallback hisCallback){
        retrieveHistory(hisCallback);
    }

    public void setHistory(String key) {
        String uid = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference ref = database.getReference("Users").child(uid).child("history");
        String Hisid = ref.push().getKey();
        DatabaseReference videoref = database.getReference("Histories").child(Hisid);
        Map<String, Object> addid = new HashMap<>();
        addid.put(Hisid, "true");
//                                    videoid
        Map<String, Object> addHis = new HashMap<>();
        addHis.put("date", getCurrentTimeStamp());
        addHis.put("video", key);
        ref.updateChildren(addid);
        videoref.updateChildren(addHis);
    }

    public void deletehis(String hiskey) {

        if (!hiskey.isEmpty() || !hiskey.equals("")) {
            String uid = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference favRef = database.getReference("Histories").child(hiskey);
            DatabaseReference userFavRef = database.getReference("Users").child(uid).child("history").child(hiskey);

            favRef.removeValue();
            userFavRef.removeValue();

        }
    }

    private void retrieveHistory(final HisCallback hisCallback) {

        DatabaseReference ref = database.getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("history");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        getHisDate(child.getKey(),hisCallback);
//                        histories.add(child.getKey());
                    }

//                    adapter.notifyDataSetChanged();
                }
//                progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getHisDate(final String key,final HisCallback hisCallback) {

        DatabaseReference Hisref = database.getReference("Histories").child(key);
        Hisref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Histories histories = new Histories();
                    histories.setHistoryId(key);
//                    String date = "";
                    String id = "";
                    Long time = 0L;
                    Log.d("test", "step");

//
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("date")) {
                            time = Long.valueOf(child.getValue().toString());
                            histories.setHistoryDate(getDate(time));
                        }
                        if (child.getKey().equals("video")) {
                            id = child.getValue().toString();
                            histories.getVideos().setVideoID(id);
                        }
                    }
                    getVideoInform(histories,hisCallback);

//                        videos.add(new Videos(key,name, url, getDate(time)));
//                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getVideoInform(final Histories HisVideo, final HisCallback hisCallback) {
        DatabaseReference videoRef = database.getReference().child("Videos").child(HisVideo.getVideos().getVideoID());
        videoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Histories temV=HisVideo;
                    String url = "";
                    String name = "";
                    Log.d("test", "step");

//
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("URL")) {
                            url = child.getValue().toString();
                            temV.getVideos().setVideo(url);
                        }
                        if (child.getKey().equals("name")) {
                            name = child.getValue().toString();
                            temV.getVideos().setName(name);
                        }
                    }
                    histories.add(temV);
                    hisCallback.onCallback(histories);
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
