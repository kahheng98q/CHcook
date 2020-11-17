package com.example.chcook.KahHeng.EndUser.GUI.VideoAndRecipeManagement;


import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.chcook.KahHeng.EndUser.GUI.Home;
import com.example.chcook.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadVideo extends Fragment {
    private String videouri=null;
    private StorageReference videoRef;
    private Button btnUpload;
//    private Button select;
    private View view;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
//    private VideoView vv;
//    private MediaController mc;

    private PlayerView playerView = null;
    private SimpleExoPlayer simpleExoPlayer = null;
    private ImageView fullScreenButton=null;
    private  Boolean fullscreen=false;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private Spinner spinner=null;
    private long realDurationMillis;
    //    private StorageReference videoRef;
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
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        spinner = view.findViewById(R.id.spinnerReview);
//        select = view.findViewById(R.id.button);
        nametxt = view.findViewById(R.id.name);
        desctxt = view.findViewById(R.id.desc);
        btnUpload = view.findViewById(R.id.button2);
        btnUpload.setEnabled(true);
        desctxt.setEnabled(true);
        nametxt.setEnabled(true);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            videouri = bundle.getString("key");
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.Categories, R.layout.style_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        playVideo(videouri);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        return view;
    }
    private void playVideo(String uri) {
        playerView = view.findViewById(R.id.videoImage);
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext());
        fullScreenButton=playerView.findViewById(R.id.exo_fullscreen_icon);

        playerView.setPlayer(simpleExoPlayer);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "Cookish"));


        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(uri));
        simpleExoPlayer.prepare(videoSource);
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//                if (Player.STATE_BUFFERING == playbackState) {
//                    progressBar.setVisibility(View.VISIBLE);
//                } else {
//                    progressBar.setVisibility(View.GONE);
//                }
//                long realDurationMillis = simpleExoPlayer.getDuration();
//                String time=String.format("%02d:%02d:%02d",
//                        TimeUnit.MILLISECONDS.toHours(realDurationMillis),
//                        TimeUnit.MILLISECONDS.toMinutes(realDurationMillis) -
//                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(realDurationMillis)), // The change is in this line
//                        TimeUnit.MILLISECONDS.toSeconds(realDurationMillis) -
//                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(realDurationMillis)));
                realDurationMillis = simpleExoPlayer.getDuration();
//                Toast.makeText(getActivity(),convert(realDurationMillis)
//                        , Toast.LENGTH_SHORT).show();
            }
        });

        fullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullscreen) {
                    fullScreenButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.exo_controls_fullscreen_enter));
                    getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    if(((AppCompatActivity)getActivity()).getSupportActionBar() != null){
                        ((AppCompatActivity)getActivity()). getSupportActionBar().show();
                    }
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = (int) ( 200 *getContext().getResources().getDisplayMetrics().density);
                    playerView.setLayoutParams(params);
                    fullscreen = false;
                }else{
                    fullScreenButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.exo_controls_fullscreen_exit));
                    getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    if(((AppCompatActivity)getActivity()).getSupportActionBar() != null){
                        ((AppCompatActivity)getActivity()). getSupportActionBar().hide();
                    }
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    playerView.setLayoutParams(params);
                    fullscreen = true;
                }
            }
        });
    }
//    public String convert(long miliSeconds)
//    {
//        int hrs = (int) TimeUnit.MILLISECONDS.toHours(miliSeconds) % 24;
//        int min = (int) TimeUnit.MILLISECONDS.toMinutes(miliSeconds) % 60;
//        int sec = (int) TimeUnit.MILLISECONDS.toSeconds(miliSeconds) % 60;
//        return String.format("%02d:%02d:%02d", hrs, min, sec);
//    }


    private String getFileExtension(){
        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(Uri.parse(videouri)));
    }
    private void upload() {
        Toast.makeText(getActivity(), "Please wait few minutes for uploading", Toast.LENGTH_SHORT).show();
        final String desc=desctxt.getText().toString();
        final String name=nametxt.getText().toString();
        final String category=spinner.getSelectedItem().toString();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        String uid=firebaseAuth.getCurrentUser().getUid();

        if (videouri != null && !desc.isEmpty() && !name.isEmpty()) {
            videoRef = storageReference.child("/Video/" + uid + "/" + System.currentTimeMillis() + "."+getFileExtension());
            UploadTask uploadTask = videoRef.putFile(Uri.parse(videouri));
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Network connection has problem", Toast.LENGTH_SHORT).show();
                    btnUpload.setEnabled(true);
                    desctxt.setEnabled(true);
                    nametxt.setEnabled(true);
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
                                    addURL.put("Duration",realDurationMillis);
                                    addURL.put("URL", downloadUrl);
                                    addURL.put("name",name);
                                    addURL.put("description",desc);
                                    addURL.put("Category",category);
                                    addURL.put("view",0);

                                    addURL.put("Uploaddate",getCurrentTimeStamp());
//                                    addURL.put("id",videoid);
                                    ref.updateChildren(addid);
                                    videoref.updateChildren(addURL);
                                }

                            });

                        }

                    }
                    Toast.makeText(getActivity(), "Upload Successful", Toast.LENGTH_SHORT).show();
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.myNavHostFragment, new VideoManagement());
                    fragmentTransaction.addToBackStack(null).commit();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    updateProgress(taskSnapshot);
                    desctxt.setEnabled(false);
                    nametxt.setEnabled(false);
                    btnUpload.setEnabled(false);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Select Video and fill in the information", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    private long getCurrentTimeStamp() {
        long timestamp=System.currentTimeMillis()/1000;
        return timestamp;
    }
    private void updateProgress(UploadTask.TaskSnapshot taskSnapshot) {
        long fileSize = taskSnapshot.getTotalByteCount();
        long uploadbytes = taskSnapshot.getBytesTransferred();
        long progress = (100 * uploadbytes) / fileSize;
        ProgressBar progressBar = view.findViewById(R.id.progressBar1);
        progressBar.setProgress((int) progress);
    }

    @Override
    public void onStop() {
        super.onStop();
        simpleExoPlayer.stop();
    }

}
