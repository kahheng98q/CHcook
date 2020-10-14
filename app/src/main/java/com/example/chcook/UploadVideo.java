package com.example.chcook;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadVideo extends Fragment {
    private Uri videouri;
    private StorageReference videoRef;
    private Button up;
    private Button select;
    private View view;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private VideoView vv;
    private MediaController mc;
    public static final int Google_Sign_In_Code = 10005;

    public UploadVideo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_upload_video, container, false);
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        select=view.findViewById(R.id.button);
        up=view.findViewById(R.id.button2);



        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
//                upload();
                uploadvideo();
            }
        });
        vv=view.findViewById(R.id.videoImage);

        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mc=new MediaController(getActivity());
                        vv.setMediaController(mc);
                        mc.setAnchorView(vv);
                    }
                });
            }
        });
        vv.start();
        videoRef=storageReference.child("/video/"+uid+"userdemo2.mp4");

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        return view;
    }

    private void upload(){
        if(videouri!=null){
            UploadTask uploadTask=videoRef.putFile(videouri);
            uploadTask.addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(), "Sucess", Toast.LENGTH_SHORT).show();
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.myNavHostFragment, new StepManagement());
                    fragmentTransaction.commit();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                updateProgress(taskSnapshot);
                }
            });
        }else{
            Toast.makeText(getActivity(), "Nothing", Toast.LENGTH_SHORT).show();
        }


    }
    public void uploadvideo(){
        Intent intent=new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select a Video"),10005);
    }

    public void record(){
        Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent,10005);
    }

    private void updateProgress(UploadTask.TaskSnapshot taskSnapshot){
        long fileSize=taskSnapshot.getTotalByteCount();
        long uploadbytes=taskSnapshot.getBytesTransferred();
        long progress=(100*uploadbytes)/fileSize;
        ProgressBar progressBar=view.findViewById(R.id.progressBar1);
        progressBar.setProgress((int)progress);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==10005){
            if(resultCode==RESULT_OK&& requestCode==10005 &&data !=null){
//                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                videouri=data.getData();
                vv.setVideoURI(videouri);
            }

        }
    }
}
