package com.example.chcook.YangJie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.chcook.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ShowBanVideo extends AppCompatActivity {


    private VideoView video;
    private String vid;
    private Button ban, back;
    private int tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ban_video);
        video = findViewById(R.id.banVideo);
        ban = findViewById(R.id.btnBan);
        back = findViewById(R.id.btnBack);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            vid = extras.getString("videoId");
        }
        ban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderR = new AlertDialog.Builder(ShowBanVideo.this);
                if (tag == 1) {
                    builderR.setTitle("Recover this video");
                    builderR.setMessage("Are you sure want to recover?");
                } else {
                    builderR.setTitle("Ban this video");
                    builderR.setMessage("Are you sure want to ban?");
                }

                builderR.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Query query = FirebaseDatabase.getInstance().getReference("Videos").child(vid);

                        HashMap hashMap = new HashMap();

                        if (tag == 1) {
                            hashMap.put("Banned", "no");
                        } else {
                            hashMap.put("Banned", "yes");
                        }

                        query.getRef().updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(ShowBanVideo.this, "Success", Toast.LENGTH_SHORT).show();
//                                ban.setEnabled(false);

                            }
                        });
                    }
                });
                builderR.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialogR = builderR.create();
                dialogR.show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderR = new AlertDialog.Builder(ShowBanVideo.this);

                builderR.setTitle("Back");
                builderR.setMessage("Are you sure want to back?");


                builderR.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), StaffMainPage.class);
                        intent.putExtra("page", "banVideo");
                        startActivity(intent);
                    }
                });
                builderR.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialogR = builderR.create();
                dialogR.show();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Videos");
        DatabaseReference df = databaseReference.child(vid);
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.child("URL").getValue(String.class);
                String banned = dataSnapshot.child("Banned").getValue(String.class);
                if (banned.equals("yes")) {
                    ban.setText("unBan");
                    ban.setTag(1);
                } else {
                    ban.setText("Ban");
                    ban.setTag(2);
                }
                tag = (Integer) ban.getTag();
                Uri uri = Uri.parse(url);
                video.setVideoURI(uri);
                video.start();
                video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                            @Override
                            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                                MediaController mc = new MediaController(ShowBanVideo.this);
                                video.setMediaController(mc);
                                mc.setAnchorView(video);

                                mp.start();
                            }
                        });

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
