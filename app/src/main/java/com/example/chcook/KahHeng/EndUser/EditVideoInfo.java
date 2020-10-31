package com.example.chcook.KahHeng.EndUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Videos;
import com.example.chcook.KahHeng.EndUser.DA.VideoDA;
import com.example.chcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditVideoInfo extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private ImageView imageView;
    private EditText nametxt;
    private EditText desctxt;
    private Button editBtn;
    private String key="";
    private VideoDA videoDA= new VideoDA();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video_info);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        imageView = findViewById(R.id.editvideoImage);
        nametxt = findViewById(R.id.editNameTxt);
        desctxt = findViewById(R.id.editDescText);
        editBtn = findViewById(R.id.editButton);
        if (getIntent()!=null){
            key=getIntent().getStringExtra("Key");
            videoDA.setVideokey(key);
            videoDA.getVideoInform(new VideoDA.Callvideo() {
                @Override
                public Videos onCallVideo(Videos video) {

                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(video.getVideo())
                            .into(imageView);

                    nametxt .setText(video.getName());
                    desctxt .setText(video.getDesc());
                    return video;
                }
            });
        }


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nametxt.getText().toString();
                String desc=desctxt.getText().toString();
                if(!name.isEmpty()&&!desc.isEmpty()){
                    videoDA.editVideoNameNDesc(name,desc);
                    Toast.makeText(getApplicationContext(),"Edit Successful" , Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainPage.class));

                }else {
                    Toast.makeText(getApplicationContext(),"Name and Description can be empty" , Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}
