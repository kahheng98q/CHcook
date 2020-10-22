package com.example.chcook.KahHeng.EndUser;


import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chcook.Domain.User;
import com.example.chcook.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private EditText nametxt;
    private EditText desctxt;

    public static final int Google_Sign_In_Code = 10005;
    private AlertDialog.Builder alertBuilder;

    public UploadVideo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload_video, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        select = view.findViewById(R.id.button);
        nametxt = view.findViewById(R.id.name);
        desctxt = view.findViewById(R.id.desc);
        up = view.findViewById(R.id.button2);
        desctxt.setEnabled(true);
        nametxt.setEnabled(true);


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
//                upload();
                uploadvideo();
            }
        });
        vv = view.findViewById(R.id.videoImage);

        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mc = new MediaController(getActivity());
                        vv.setMediaController(mc);
                        mc.setAnchorView(vv);
                        mp.start();
                    }
                });
            }
        });
        vv.start();

        String uid = firebaseAuth.getCurrentUser().getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users = database.getReference("Users").child(uid);
        String id = users.push().toString();
        String[] separated = id.split("/");
        String vid = separated[separated.length - 1];
        videoRef = storageReference.child("/video/" + uid + "/" + vid + ".mp4");

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        return view;
    }

    private void upload() {
        final String desc=desctxt.getText().toString();
        final String name=nametxt.getText().toString();

        if (videouri != null && !desc.isEmpty() && !name.isEmpty()) {
            UploadTask uploadTask = videoRef.putFile(videouri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    String uid = firebaseAuth.getCurrentUser().getUid();

                                    final FirebaseDatabase database = FirebaseDatabase.getInstance();

                                    DatabaseReference ref = database.getReference("Users").child(uid).child("video");
                                    String videoid=ref.push().getKey();
                                    DatabaseReference videoref = database.getReference("Videos").child(videoid);
                                    Map<String, Object> addid = new HashMap<>();
                                    addid.put(videoid, "true");
//                                    videoid
                                    Map<String, Object> addURL = new HashMap<>();
                                    addURL.put("URL", downloadUrl);
                                    addURL.put("name",name);
                                    addURL.put("description",desc);
                                    addURL.put("view",0);
                                    addURL.put("Uploaddate",getCurrentTimeStamp());
//                                    addURL.put("id",videoid);
                                    ref.updateChildren(addid);
                                    videoref.updateChildren(addURL);
                                }

                            });

//                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
////
////
////
//
//                                }
//                            });
                        }

                    }
                    Toast.makeText(getActivity(), "Upload Successful", Toast.LENGTH_SHORT).show();
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.myNavHostFragment, new Home());
                    fragmentTransaction.commit();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    updateProgress(taskSnapshot);
                    desctxt.setEnabled(false);
                    nametxt.setEnabled(false);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Select Video and fill in the information", Toast.LENGTH_SHORT).show();
        }


    }


    private long getCurrentTimeStamp() {
        long timestamp=System.currentTimeMillis()/1000;
        return timestamp;
    }
    private void uploadvideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video"), 10005);
    }

    public void record() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, 10005);
    }

    private void updateProgress(UploadTask.TaskSnapshot taskSnapshot) {
        long fileSize = taskSnapshot.getTotalByteCount();
        long uploadbytes = taskSnapshot.getBytesTransferred();
        long progress = (100 * uploadbytes) / fileSize;
        ProgressBar progressBar = view.findViewById(R.id.progressBar1);
        progressBar.setProgress((int) progress);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10005) {
            if (resultCode == RESULT_OK && requestCode == 10005 && data != null) {
//                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                videouri = data.getData();
//                Glide.with(getActivity())
//                        .load(videouri).
//                        apply(options).
//                        into(mThumb);
                vv.setVideoURI(videouri);
            }

        }
    }
}
