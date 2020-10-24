package com.example.chcook.DA;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chcook.Domain.History;
import com.example.chcook.Domain.Videos;
import com.example.chcook.R;
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
    private View view;
    private ProgressBar progressBar;
    private ArrayList<String> histories;
    private Context context;
    private Activity activity;
    private ArrayList<Videos> videos;

    public HistoryDA() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public HistoryDA(View view, Context context, Activity activity) {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        this.context=context;
        this.view = view;
        this.activity=activity;
    }

    public interface hisCallback{
        ArrayList<History> onCallback(ArrayList<History> histories);
    }

    public void getHistoryRealData(){

    }

    public void setHistory(String key) {
        String uid = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference ref = database.getReference("Users").child(uid).child("history");
        String Hisid = ref.push().getKey();
        DatabaseReference videoref = database.getReference("History").child(Hisid);
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
//            Log.d("test", "message text:" + hiskey);

            String uid = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference favRef = database.getReference("History").child(hiskey);
            DatabaseReference userFavRef = database.getReference("Users").child(uid).child("history").child(hiskey);

            favRef.removeValue();
            userFavRef.removeValue();

        }
    }

    private void retrieveHistory() {

//        recyclerView = view.findViewById(R.id.HistoryRecyclevView);
//        recyclerView.setHasFixedSize(true);
//        adapter = new Adapter(context, videos, "history", histories);
//        layoutManager = new LinearLayoutManager(activity);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

        DatabaseReference ref = database.getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("history");
        progressBar = view.findViewById(R.id.progressBarHis);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        getHisDate(child.getKey());
                        histories.add(child.getKey());
                    }

//                    adapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getHisDate(final String key) {

        DatabaseReference Hisref = database.getReference("History").child(key);
        Hisref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Videos video = new Videos();

//                    String date = "";
                    String id = "";
                    Long time = 0L;
                    Log.d("test", "step");

//
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("date")) {
                            time = Long.valueOf(child.getValue().toString());
                            video.setDate(getDate(time));
                        }
                        if (child.getKey().equals("video")) {
                            id = child.getValue().toString();
                            video.setVideoID(id);
                        }
                    }
//                    getVideoInform(video);
//                        videos.add(new Videos(key,name, url, getDate(time)));
//                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
