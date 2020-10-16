package com.example.chcook.KahHeng.EndUser;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Videos;
import com.example.chcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


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
        progressBar=view.findViewById(R.id.progressBarPlay);
        progressBar.setVisibility(View.VISIBLE);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            key = bundle.getString("key");
        }
//        head="https://firebasestorage.googleapis.com/v0/b/chcook-30453.appspot.com/o/recipe%2Fhead.PNG?alt=media&token=23f78178-340a-49dd-8ad7-06ca772bd577";
        getVideoInform(key);
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

    private void getVideoInform(final String key) {
        DatabaseReference videoRef = database.getReference().child("Videos").child(key);
        videoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long time = 0L;
                    Log.d("test", "step");
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

}