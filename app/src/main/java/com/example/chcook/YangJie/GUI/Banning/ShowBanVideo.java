package com.example.chcook.YangJie.GUI.Banning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.chcook.R;
import com.example.chcook.YangJie.GUI.StaffMainPage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ShowBanVideo extends AppCompatActivity {


    private VideoView video;
    private String vid,desc,type,date,position,page,Vposition;
    private Button ban, back;
    private TextView rType,rDesc,rDate,title,name;
    private int tag;
    private ProgressBar pg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ban_video);
        video = findViewById(R.id.banVideo);
        name=findViewById(R.id.banVideoName);
        title = findViewById(R.id.txtBanVideoTitle);
        rType = findViewById(R.id.banVideotype);
        rDesc = findViewById(R.id.banVideoDesc);
        rDate = findViewById(R.id.banVideoDate);
        ban = findViewById(R.id.btnBan);
        back = findViewById(R.id.btnShowIncomeBack);
        pg = findViewById(R.id.progressBarShowBanVideo);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            vid = extras.getString("videoId");
            desc = extras.getString("description");
            type = extras.getString("type");
            date = extras.getString("date");
            position = extras.getString("position");
            page = extras.getString("page");

        }
        ban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderR = new AlertDialog.Builder(ShowBanVideo.this);
                final EditText input = new EditText(ShowBanVideo.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                if (tag == 1) {
                    builderR.setTitle("Unban this video");
                    builderR.setMessage("Are you sure want to recover?");
                } else {
                    builderR.setTitle("Ban this video");
                    builderR.setMessage("Please state the reason ban");
                    builderR.setView(input);
                }
                builderR.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Query query = FirebaseDatabase.getInstance().getReference("Videos").child(vid).child("Status");

                        Map<String, Object> hashMap = new HashMap<>();
                        if (tag == 1) {
                            hashMap.put("Status", "Approval");
                            hashMap.put("Reason","");

                        } else {
                            hashMap.put("Status", "Banned");
                            hashMap.put("Reason",input.getText().toString());
                            hashMap.put("Date", ServerValue.TIMESTAMP);
                        }

                        query.getRef().updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(ShowBanVideo.this, "Success", Toast.LENGTH_SHORT).show();
                                Vposition =position;
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
                        if(page.equals("staffMain")){
                            intent.putExtra("page", "main");
                        }else if(page.equals("banUser")) {
                            intent.putExtra("page", "banUser");
                        }else{
                                intent.putExtra("page", "banVideo");
                            }


                        intent.putExtra("position",position);
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
        pg.setVisibility(View.VISIBLE);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Videos");
        DatabaseReference df = databaseReference.child(vid);
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.child("URL").getValue(String.class);
               String banned = null;
                String videoName = dataSnapshot.child("name").getValue(String.class);
                String videoType = dataSnapshot.child("Category").getValue(String.class);
                String videoDesc = dataSnapshot.child("description").getValue(String.class);
                if (dataSnapshot.hasChild("Status")){
                     banned = dataSnapshot.child("Status").child("Status").getValue(String.class);
                    if (banned.equals("Banned")) {
                        ban.setText("unBan");
                        ban.setTag(1);
                    } else {
                        ban.setText("Ban");
                        ban.setTag(2);
                    }
                }else{
                    ban.setText("Ban");
                    ban.setTag(2);
                }

                if(page.equals("staffMain")){
                    title.setText("Video Details");
                    rType.setText("Video Name : " + videoName);
                    rDate.setText("Video Type : " + videoType);
                    rDesc.setText("Description : " +videoDesc );

                }else {
                    title.setText("Report Details");
                    name.setText("Video Name : " + videoName);
                    if(desc==null){
                        rDesc.setText("Description : " );
                    }else{
                        rDesc.setText("Description : " + desc);
                    }
                    rType.setText("Report Type : " + type);
                    rDate.setText("Report Date : " + date);

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
                                pg.setVisibility(View.INVISIBLE);
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
