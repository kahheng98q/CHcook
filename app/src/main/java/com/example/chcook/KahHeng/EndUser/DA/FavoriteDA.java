package com.example.chcook.KahHeng.EndUser.DA;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chcook.Domain.Favorite;
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

public class FavoriteDA {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private ArrayList<Favorite> favorites = new ArrayList<>();
    private String favoriteKey="";
    private String videokey="";

    public FavoriteDA() {
        connectFirebase();
    }

    public void setVideokey(String videokey) {
        this.videokey = videokey;
    }

    public FavoriteDA(String videokey) {
        connectFirebase();
        this.videokey=videokey;
    }

    public String getFavoriteKey() {
        return favoriteKey;
    }

    public void setFavoriteKey(String favoriteKey) {
        this.favoriteKey = favoriteKey;
    }

    public void connectFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public interface FavCheck {
        Boolean onValidFav(Boolean addedList);
    }
    public interface CallFavorite {
        ArrayList<Favorite> onCallback(ArrayList<Favorite> favorites);
    }

    public void retrieveFavorite(final CallFavorite callFavorite) {

        DatabaseReference ref = database.getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Favorite");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.d("test", "favstep");
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        getFavDate(child.getKey(),callFavorite);
                    }

                }else {
                    callFavorite.onCallback(new ArrayList<Favorite>());
                }
//                progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getFavDate(final String key,final CallFavorite callFavorite) {

        DatabaseReference Hisref = database.getReference("Favorite").child(key);
        Hisref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Favorite favorite = new Favorite();
                    favorite.setFavoriteId(key);
                    String id = "";
                    Long time = 0L;
//                    Log.d("test", "step");

//
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("Date")) {
                            time = Long.valueOf(child.getValue().toString());
                            favorite.setFavoriteDate(getDate(time));
                        }
                        if (child.getKey().equals("Video")) {
                            id = child.getValue().toString();
                            favorite.getVideos().setVideoID(id);
                        }
                    }
                    getFavVideoInform(favorite,callFavorite);

//                        videos.add(new Videos(key,name, url, getDate(time)));
//                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getFavVideoInform(final Favorite favVideo, final CallFavorite callFavorite) {
        DatabaseReference videoRef = database.getReference().child("Videos").child(favVideo.getVideos().getVideoID());
        videoRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Favorite temF=favVideo;
                    String url = "";
                    String name = "";


//
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("URL")) {
                            url = child.getValue().toString();
                            temF.getVideos().setVideo(url);
                        }
                        if (child.getKey().equals("name")) {
                            name = child.getValue().toString();
                            temF.getVideos().setName(name);
                        }
                    }
                    favorites.add(temF);
                }
                callFavorite.onCallback(favorites);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
        ;

    }

    public void CheckFav(final FavCheck favCheck) {

        DatabaseReference ref = database.getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Favorite");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        CheckVideo(child.getKey().toString(),favCheck);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void CheckVideo (final String favkey,final FavCheck favCheck) {
        DatabaseReference Hisref = database.getReference("Favorite").child(favkey);
//        Log.d("test","aaaa:"+videokey);

        Hisref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean booleanfav=false;
                if (dataSnapshot.exists()) {
//                    Log.d("test",child.getValue().toString());
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("Video")) {

                            Log.d("test",child.getValue().toString());
                            if (child.getValue().equals(videokey)) {
//                                Log.d("test",child.getValue().toString());
//                                btnFav.setBackgroundResource(R.drawable.ic_star_bright_24dp);
                                favoriteKey =favkey;
                                booleanfav=true;
                            }

                        }

                    }

                }
                favCheck.onValidFav(booleanfav);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean deleteFav() {

        if (!favoriteKey.isEmpty() || !favoriteKey.equals("")) {
//            Log.d("test", "message text:" + favoriteKey);
            String uid = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference favRef = database.getReference("Favorite").child(favoriteKey);
            DatabaseReference userFavRef = database.getReference("Users").child(uid).child("Favorite").child(favoriteKey);
            favRef.removeValue();
            userFavRef.removeValue();

        } else {
            Log.d("test", "message text:null");
        }
        return  false;
    }
    public void setFavorite() {
        String uid = firebaseAuth.getCurrentUser().getUid();
        final DatabaseReference userRef = database.getReference().child("Users").child(uid).child("Favorite");

        String favId = userRef.push().getKey();
        DatabaseReference favRef = database.getReference("Favorite").child(favId);

        Map<String, Object> addFav = new HashMap<>();
        addFav.put("Video", videokey);
        addFav.put("Date", getCurrentTimeStamp());
        favoriteKey=favId;
        Map<String, Object> addFavID = new HashMap<>();
        addFavID.put(favId, true);
        favRef.updateChildren(addFav);
        userRef.updateChildren(addFavID);
//        return favId;
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
