package com.example.chcook.KahHeng.EndUser;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Videos;
import com.example.chcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayVideo extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private View view;
    private VideoView imageView;
    //    private ImageView imageView;
    private ImageView userPro;
    private TextView txtView;
    private TextView txtDate;
    private TextView txtName;
    private TextView txtUser;
    private TextView txtdesc;
    private Button btnFav;
    private Button btnReport;
    private String url;
    private String head;
    private String key;
    private ArrayList<Videos> videos = new ArrayList<>();
    private MediaController mc;
    private ProgressBar progressBar;

    public PlayVideo() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_play_video, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
//        videos.add(new Videos("1","Fried Rice","3K","01/08/2020","https://firebasestorage.googleapis.com/v0/b/chcook-30453.appspot.com/o/recipe%2FChicken%20rice.PNG?alt=media&token=d21f8eed-8c2a-4cd7-84a0-e022f5e2facc","Heng","This is nice"));
        imageView = view.findViewById(R.id.videoImage);
        txtView = view.findViewById(R.id.txtView);
        txtDate = view.findViewById(R.id.txtDate);
        txtName = view.findViewById(R.id.txtName);
        txtUser = view.findViewById(R.id.txtUsername);
        txtdesc = view.findViewById(R.id.txtDesc);
        userPro = view.findViewById(R.id.userHead);
        btnFav=view.findViewById(R.id.btnFavorite);
        btnReport=view.findViewById(R.id.btnReport);

        progressBar = view.findViewById(R.id.progressBarPlay);
        progressBar.setVisibility(View.VISIBLE);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            key = bundle.getString("key");
        }
//        head="https://firebasestorage.googleapis.com/v0/b/chcook-30453.appspot.com/o/recipe%2Fhead.PNG?alt=media&token=23f78178-340a-49dd-8ad7-06ca772bd577";
        getVideoInform(key);
        setUser(key);
//        setVideo();
        imageView.start();
        imageView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mc = new MediaController(getActivity());
                        imageView.setMediaController(mc);
                        mc.setAnchorView(imageView);
                        progressBar.setVisibility(View.GONE);
                        mp.start();
                    }
                });
            }
        });
        imageView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                    progressBar.setVisibility(View.GONE);
                }
                if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                    progressBar.setVisibility(View.GONE);
                }
                return false;
            }
        });

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private void setVideo() {
//        Glide.with(getContext())
//                .asBitmap()
//                .load(videos.get(0).getImage())
//                .into(imageView);
//        Glide.with(getContext())
//                .asBitmap()
//                .load(head)
//                .into(userPro);
//        txtView.setText(videos.get(0).getView() + " Views");
//        txtDate.setText(videos.get(0).getDate());
//        txtUser.setText(videos.get(0).getUser());
    }

    private void addview() {

    }

    private void getVideoInform(final String key) {
        final DatabaseReference videoRef = database.getReference().child("Videos").child(key);
        setHistory(key);

        videoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long time = 0L;

                    if (dataSnapshot.exists()) {

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.getKey().equals("URL")) {
                                url = child.getValue().toString();
                                imageView.setVideoURI(Uri.parse(url));
                            }
                            if (child.getKey().equals("name")) {
                                txtName.setText(child.getValue().toString());
                            }
                            if (child.getKey().equals("description")) {
                                txtdesc.setText(child.getValue().toString());
                            }
                            if (child.getKey().equals("Uploaddate")) {
                                time = Long.valueOf(child.getValue().toString());
                                txtDate.setText(getDate(time));
                            }
                            if (child.getKey().equals("view")) {
                                int view = 0;
                                view = Integer.valueOf(child.getValue().toString()) + 1;
                                txtView.setText("" + view + "View");
                                Map<String, Object> addView = new HashMap<>();
                                addView.put("view", view);
                                videoRef.updateChildren(addView);
                            }
                        }
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

    private void setHistory(String key) {
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

    private void setUser(final String key) {
        DatabaseReference ref = database.getReference().child("Users");
        ref.orderByChild("video").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    if (child.getKey().equals("video")) {
                        Map<String, Boolean> videoMap = (Map<String, Boolean>) child.getValue();

                        for (String userVideoKey : videoMap.keySet()) {
//                              Log.d("test", "message text:"+key);
                            if (userVideoKey.equals(key)) {
                                Log.d("test", "message text:" + dataSnapshot.getKey());
                                setUserInfo(dataSnapshot.getKey());
                                break;
                            }
                        }

                    }

                }
//                dataSnapshot.getRef();
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
//                String Hisid=ref.push().getKey();
//        DatabaseReference videoref = database.getReference("History").child(Hisid);
//        Map<String, Object> addid = new HashMap<>();
//        addid.put(Hisid, "true");
////                                    videoid
//        Map<String, Object> addHis = new HashMap<>();
//        addHis.put("date",getCurrentTimeStamp());
//        addHis.put("video",key);
//        ref.updateChildren(addid);
//        videoref.updateChildren(addHis);
    }

    private void setUserInfo(final String key) {
        DatabaseReference userRef = database.getReference().child("Users").child(key);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String url = "";
                    String name = "";


                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("Image")) {
                            Glide.with(getContext())
                                    .asBitmap()
                                    .load(child.getValue().toString())
                                    .into(userPro);

                        }
                        if (child.getKey().equals("Name")) {
                            name = child.getValue().toString();
                            txtUser.setText(name);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}