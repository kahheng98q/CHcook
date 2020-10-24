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
import com.example.chcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditVideo extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private ImageView imageView;
    private EditText nametxt;
    private EditText desctxt;
    private Button editBtn;
    private String key="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);
//
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        imageView = findViewById(R.id.editvideoImage);
        nametxt = findViewById(R.id.editNameTxt);
        desctxt = findViewById(R.id.editDescText);
        editBtn = findViewById(R.id.editButton);
        if (getIntent()!=null){
            key=getIntent().getStringExtra("Key");
        }

        getVideoInform(key);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nametxt.getText().toString();
                String desc=desctxt.getText().toString();
                if(!name.isEmpty()&&!desc.isEmpty()){
                    DatabaseReference videoRef = database.getReference().child("Videos").child(key);

                    Map<String, Object> videoUpdates = new HashMap<>();
                    videoUpdates.put("name",name );
                    videoUpdates.put("description", desc);
                    videoRef.updateChildren(videoUpdates);
                    Toast.makeText(getApplicationContext(),"Edit Successful" , Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainPage.class));

                }else {
                    Toast.makeText(getApplicationContext(),"Name and Description can be empty" , Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void getVideoInform(final String key) {
        DatabaseReference videoRef = database.getReference().child("Videos").child(key);
        videoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    Log.d("test", "step");
                    if (dataSnapshot.exists()) {

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.getKey().equals("URL")) {
                                Glide.with(getApplicationContext())
                                        .asBitmap()
                                        .load(child.getValue().toString())
                                        .into(imageView);
                            }
                            if (child.getKey().equals("name")) {
                                nametxt .setText(child.getValue().toString());
                            }
                            if (child.getKey().equals("description")) {
                                desctxt .setText(child.getValue().toString());
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
//                getVideoInform();

    }
}
