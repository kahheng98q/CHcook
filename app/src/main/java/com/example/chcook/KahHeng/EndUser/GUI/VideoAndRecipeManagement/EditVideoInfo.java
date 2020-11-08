package com.example.chcook.KahHeng.EndUser.GUI.VideoAndRecipeManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Videos;
import com.example.chcook.KahHeng.EndUser.DA.VideoDA;
import com.example.chcook.KahHeng.EndUser.GUI.MainPage;
import com.example.chcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class EditVideoInfo extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private ImageView imageView;
    private EditText nametxt;
    private EditText desctxt;
    private Button editBtn;
    private String key="";
    private VideoDA videoDA= new VideoDA();
    private Spinner spinner=null;
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
        spinner = findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Categories, R.layout.style_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
//        spinner.setSelection(0);
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
                    setSpinText(video.getCategory());
                    return video;
                }
            });
        }


        editBtn.setOnClickListener(v -> {
            String name=nametxt.getText().toString();
            String desc=desctxt.getText().toString();
            String cate=spinner.getSelectedItem().toString();
            if(!name.isEmpty()&&!desc.isEmpty()){
                videoDA.editVideoNameNDesc(name,desc,cate);

                Toast.makeText(getApplicationContext(),"Edit Successful" , Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainPage.class));

            }else {
                Toast.makeText(getApplicationContext(),"Name and Description can be empty" , Toast.LENGTH_SHORT).show();
            }

        });

    }
    public void setSpinText(String text)
    {
        for(int i= 0; i < spinner.getAdapter().getCount(); i++)
        {
            if(spinner.getAdapter().getItem(i).toString().contains(text))
            {
                spinner.setSelection(i);
            }
        }

    }
}
