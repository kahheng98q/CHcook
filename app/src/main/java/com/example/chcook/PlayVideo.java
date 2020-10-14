package com.example.chcook;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Videos;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayVideo extends Fragment {
    private View view;
    private ImageView imageView;
    private ImageView userPro;
    private TextView txtView;
    private TextView txtDate;
    private TextView txtName;
    private TextView txtUser;
    private TextView txtdesc;

    private String head;
    private ArrayList<Videos> videos =new ArrayList<>();

    public PlayVideo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_play_video, container, false);

        videos.add(new Videos("1","Fried Rice","3K","01/08/2020","https://firebasestorage.googleapis.com/v0/b/chcook-30453.appspot.com/o/recipe%2FChicken%20rice.PNG?alt=media&token=d21f8eed-8c2a-4cd7-84a0-e022f5e2facc","Heng","This is nice"));
        imageView=view.findViewById(R.id.videoImage);
        txtView=view.findViewById(R.id.txtView);
        txtDate=view.findViewById(R.id.txtDate);
        txtName=view.findViewById(R.id.txtName);
        txtUser=view.findViewById(R.id.txtUsername);
        txtdesc=view.findViewById(R.id.txtDesc);
        userPro=view.findViewById(R.id.userHead);
        head="https://firebasestorage.googleapis.com/v0/b/chcook-30453.appspot.com/o/recipe%2Fhead.PNG?alt=media&token=23f78178-340a-49dd-8ad7-06ca772bd577";

        setVideo();

        return view;
    }

    private void setVideo(){
        Glide.with(getContext())
                .asBitmap()
                .load(videos.get(0).getImage())
                .into(imageView);
        Glide.with(getContext())
                .asBitmap()
                .load(head)
                .into(userPro);
        txtView.setText(videos.get(0).getView()+" Views");
        txtDate.setText(videos.get(0).getDate());
        txtName.setText(videos.get(0).getName());
        txtUser.setText(videos.get(0).getUser());
        txtdesc.setText(videos.get(0).getDesc());
    }

}
