package com.example.chcook.KahHeng.EndUser.DA;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chcook.Domain.Ratings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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

public class RatingDA {
    private String videoKey = "";

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private String ratingkey = "";
    private ArrayList<Ratings> ratings = new ArrayList<>();

    public String getRatingkey() {
        return ratingkey;
    }

    public void setRatingkey(String ratingkey) {
        this.ratingkey = ratingkey;
    }


    public void connectFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public RatingDA() {
        connectFirebase();
    }

    public interface RatingCallback {
        void onCallback(ArrayList<Ratings> ratings, double rating, int numofRate);
    }

    public interface onCheckRate {
        void onCallback(double ratingGiven);
    }

    public void getAvgRating(final RatingCallback ratingCallback) {
        DatabaseReference ref = database.getReference("Videos").child(videoKey).child("Rating");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        setRatingkey(child.getKey());
                        getAllRate(ratingCallback);

                    }

                } else {
                    ratingCallback.onCallback(ratings, 0, 0);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAllRate(final RatingCallback ratingCallback) {
        DatabaseReference videoRef = database.getReference().child("Rating").child(ratingkey);
        videoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long time = 0L;
                    double rate = 0;
                    double rating = 0;
                    int numOfRating = 0;
                    double avg = 0;
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.getKey().equals("Rating")) {
                                rating = Double.parseDouble(child.getValue().toString());
                                numOfRating = numOfRating + 1;
                            }
                            if (child.getKey().equals("Date")) {
                                time = Long.valueOf(child.getValue().toString());
                            }
                            rate = rating + rate;

                        }
                        String id = dataSnapshot.getKey();

                        ratings.add(new Ratings(id, rating, getDate(time)));
                        if (numOfRating != 0) {
                            avg = rate / (double)numOfRating;
                            Log.d("test5", "message rate:" + rate);
                            Log.d("test5", "message num:" + numOfRating);
                            Log.d("test5", "message avg:" + avg);
                            ratingCallback.onCallback(ratings, avg, numOfRating);
                        }

                    }


                    ratingCallback.onCallback(ratings, avg, numOfRating);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
        ;
    }

    public double getavgRate(ArrayList<Ratings> ratingList){
        double rate = 0;
        int numOfRating = ratingList.size();
        double avg = 0;
        for (Ratings tmprating : ratingList) {
            rate = tmprating.getRating() + rate;

        }
        if (numOfRating != 0) {
            avg = rate / (double)numOfRating;
//            Log.d("test5", "message rate:" + rate);
//            Log.d("test5", "message num:" + numOfRating);
//            Log.d("test5", "message avg:" + avg);
//            ratingCallback.onCallback(ratings, avg, numOfRating);
        }
        return avg;
    }

    public void CheckRating(final onCheckRate onCheckRate) {
        DatabaseReference ref = database.getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Rating");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
//                    Log.d("test5", "message textAAAAAAA:" + ratings.size());
                    if (ratings.size()>0){
//                        Log.d("test5", "message textAAAAAAA:" + "GGGGGGGGGGGGGGGGGGGGGGGGg");
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                            checkRate(child.getKey(), onCheckRate);
                        }
                    }else {
//                        Log.d("test5", "message textAAAAAAA:" + "GGGGGGGGGGGGGGGGGGGGGGGGg");
                        onCheckRate.onCallback(0);
                    }


                } else {
                    onCheckRate.onCallback(0);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkRate(final String rateid, final onCheckRate onCheckRate) {
        DatabaseReference Hisref = database.getReference("Rating").child(rateid);
        Hisref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//                    Log.d("test5", "message textAAAAAAA:" + "2");

                    String id = dataSnapshot.getKey();

                    for (Ratings rating : ratings) {
//                        Log.d("test5", "message textAAAAAAA:" + "3");
//                        Log.d("test5", "message textAAAAAAA:" + "GGGGGGGGGGGGGGGGGGGGGGGGg");
                        if (rating.getRateId().equals(id)) {
                            onCheckRate.onCallback(rating.getRating());
                        }
                    }
                } else {
                    onCheckRate.onCallback(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
//    public void CheckRating(final onCheckRate onCheckRate) {
//        DatabaseReference ref = database.getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
//        ref.orderByChild("Rating").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    if (child.getKey().equals("Rating")) {
//                        Map<String, Boolean> videoMap = (Map<String, Boolean>) child.getValue();
////                        Log.d("test", "message text:"+child.getKey());
//                        for (String userVideoKey : videoMap.keySet()) {
//                            Log.d("test5", "message text:"+userVideoKey);
//
////
////                            for (Ratings rating : ratings){
////                                Log.d("test5", "message textAAAAAAA:"+"GGGGGGGGGGGGGGGGGGGGGGGGg");
////
////                                if (rating.getRateId().equals(userVideoKey)){
////                                    onCheckRate.onCallback(rating.getRating());
////                                }
////                            }
//                        }
//
//                    }
//
//                }
////                dataSnapshot.getRef();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//    private void getAllRate2(final RatingCallback ratingCallback) {
//        DatabaseReference videoRef = database.getReference().child("Rating").child(ratingkey);
//        videoRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    Long time = 0L;
//                    double rate = 0;
//                    double rating = 0;
//                    int numOfRating = 0;
//                    double avg = 0;
//                    if (dataSnapshot.exists()) {
//                        for (DataSnapshot child : dataSnapshot.getChildren()) {
//                            if (child.getKey().equals("Rating")) {
//                                rating = Double.parseDouble(child.getValue().toString());
//                                numOfRating = numOfRating + 1;
//                            }
//                            if (child.getKey().equals("Date")) {
//                                time = Long.valueOf(child.getValue().toString());
//                            }
//                            rate = rating + rate;
//
//                        }
//                        String id = dataSnapshot.getKey();
//
//                        ratings.add(new Ratings(id, rating, getDate(time)));
//                    }
//                    if (numOfRating != 0) {
//                        avg = rate / numOfRating;
//                    }
//
//                    ratingCallback.onCallback(ratings, avg, numOfRating);
//                }
//
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        })
//        ;
//    }

    public void giveRating(String rating) {
        String uid = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference ref = database.getReference("Users").child(uid).child("Rating");
        String rateId = ref.push().getKey();
        DatabaseReference rateref = database.getReference("Rating").child(rateId);
        DatabaseReference videoref = database.getReference("Videos").child(videoKey).child("Rating");
        Map<String, Object> addid = new HashMap<>();
        addid.put(rateId, "true");

        Map<String, Object> addURL = new HashMap<>();
        addURL.put("Rating", rating);
        addURL.put("Date", getCurrentTimeStamp());


        ref.updateChildren(addid);
        videoref.updateChildren(addid);
        rateref.updateChildren(addURL);
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
