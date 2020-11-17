package com.example.chcook.KahHeng.EndUser.GUI.VideoPlaying;


import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.Rating;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Ratings;
import com.example.chcook.Domain.User;
import com.example.chcook.KahHeng.EndUser.DA.FavoriteDA;
import com.example.chcook.KahHeng.EndUser.DA.HistoryDA;
import com.example.chcook.KahHeng.EndUser.DA.RatingDA;
import com.example.chcook.KahHeng.EndUser.DA.UserDA;
import com.example.chcook.KahHeng.EndUser.DA.VideoDA;
import com.example.chcook.Domain.Videos;
import com.example.chcook.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayVideo extends Fragment{
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private View view;

    //    private VideoView imageView;
    private TextView txtRate;
    private TextView txtView;
    private TextView txtDate;
    private TextView txtName;
    private TextView txtdesc;
    private TextView txtCategory;
    private ImageView userPro;
    private TextView txtUser;
    private ImageButton btnFav;
    private ImageButton btnReport;
    private String url;
    private String head;
    private String key;
    private ArrayList<Videos> videos = new ArrayList<>();
    //    private MediaController mc;
    private ProgressBar progressBar;

    private Boolean fav = false;
    private String pfavKey = "";
    private EditText editTextDesc;
    private RadioGroup rgReportTitle;
    private RadioButton rbReportTitle;
    private Button btnSendReport;
    private Button btnCancel;
    private HistoryDA historyDA;
    private VideoDA videoDA = new VideoDA();
    private RatingDA ratingDA = new RatingDA();
    private ArrayList<Ratings> ratings = new ArrayList<>();
    private double givenRate = 0;
    private UserDA userDA = new UserDA();

    private PlayerView playerView = null;
    private SimpleExoPlayer simpleExoPlayer = null;
    private ImageView fullScreenButton=null;
    private  Boolean fullscreen=false;
    boolean isVisible = false;
    public PlayVideo () {
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
        historyDA = new HistoryDA();
//        videos.add(new Videos("1","Fried Rice","3K","01/08/2020","https://firebasestorage.googleapis.com/v0/b/chcook-30453.appspot.com/o/recipe%2FChicken%20rice.PNG?alt=media&token=d21f8eed-8c2a-4cd7-84a0-e022f5e2facc","Heng","This is nice"));

//        imageView = view.findViewById(R.id.videoImage);
        txtView = view.findViewById(R.id.txtView);
        txtDate = view.findViewById(R.id.txtDate);
        txtName = view.findViewById(R.id.addName);

        txtUser = view.findViewById(R.id.txtUsername);
        txtdesc = view.findViewById(R.id.txtDesc);
        userPro = view.findViewById(R.id.userHead);
        txtCategory= view.findViewById(R.id.txtCategory);
        btnFav = view.findViewById(R.id.btnFavorite);
        btnReport = view.findViewById(R.id.btnReport);

        txtRate = view.findViewById(R.id.txtRate);
        progressBar = view.findViewById(R.id.progressBarPlay);
        progressBar.setVisibility(View.VISIBLE);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            key = bundle.getString("key");
            ratingDA.setVideoKey(key);
            ratingDA.getAvgRating(new RatingDA.RatingCallback() {
                @Override
                public void onCallback(ArrayList<Ratings> retrievedRatings, double rating, int numofRate) {
                    ratings = retrievedRatings;
//                    Log.d("test5", "message textAAAAAAA:" + ratings.size());
//                    ratingDA.getavgRate(retrievedRatings);

                    txtRate.setText(String.format("%.1f",  ratingDA.getavgRate(retrievedRatings)));
                    ratingDA.CheckRating(new RatingDA.onCheckRate() {
                        @Override
                        public void onCallback(double ratingGiven) {
                            givenRate = ratingGiven;

//                            Toast.makeText(getContext(), "" + givenRate, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        videoDA = new VideoDA(key);
        videoDA.getVideoRealData(new VideoDA.Callvideo() {
            @Override
            public Videos onCallVideo(Videos video) {
                txtdesc.setText(video.getDesc());
                txtName.setText(video.getName());
//                imageView.setVideoURI(Uri.parse(video.getVideo()));
                playVideo(video.getVideo());
                txtDate.setText(video.getDate());
                txtView.setText(video.getView());
                txtCategory.setText("Category: "+video.getCategory());
                return video;
            }
        });

//        setUser(key);
        userDA.setVideokey(key);
        userDA.setUserBasedOnVideo(new UserDA.UserCallback() {
            @Override
            public User onCallback(User user) {
                Glide.with(getContext())
                        .asBitmap()
                        .load(user.getImage())
                        .into(userPro);
                txtUser.setText(user.getName());
                return user;
            }
        });
        historyDA.setHistory(key);
        final FavoriteDA favoriteDA = new FavoriteDA(key);
        favoriteDA.CheckFav(new FavoriteDA.FavCheck() {
            @Override
            public Boolean onValidFav(Boolean addedList) {
//                Toast.makeText(getContext(), addedList.toString(), Toast.LENGTH_SHORT).show();
                if (addedList == true) {
                    btnFav.setBackgroundResource(R.drawable.ic_star_bright_24dp);
                    fav = addedList;
                }

                return addedList;
            }
        });


        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), fav.toString(), Toast.LENGTH_SHORT).show();
                if (fav == false) {
//                    setFavorite(key);
                    favoriteDA.setFavorite();
                    btnFav.setBackgroundResource(R.drawable.ic_star_bright_24dp);
                    fav = true;
                    Toast.makeText(getContext(), "Add to favorite list", Toast.LENGTH_SHORT).show();
                } else {
                    favoriteDA.deleteFav();
                    btnFav.setBackgroundResource(R.drawable.ic_star_grey_24dp);
                    fav = false;
                    Toast.makeText(getContext(), "Removed from favorite list", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReportDialog();
            }
        });
        txtRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (givenRate >= 1) {
                    Toast.makeText(getContext(), "This video's rating has been given.", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getContext(), "" + givenRate, Toast.LENGTH_SHORT).show();
                    setReview();
                }

            }
        });
        getActivity().setTitle("Play Video");
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
                if (Player.STATE_BUFFERING == playbackState) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
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
//        if(!isVisible)
//            simpleExoPlayer.stop();

    }



    private long getCurrentTimeStamp() {
        long timestamp = System.currentTimeMillis() / 1000;
        return timestamp;
    }

    // GUI
    private void setReportDialog() {

        final AlertDialog dialogReport;
        final AlertDialog.Builder dialogBuilderReport = new AlertDialog.Builder(getContext());
        final View popReport = getLayoutInflater().inflate(R.layout.pop_window_report, null);
        editTextDesc = popReport.findViewById(R.id.txtReportDesc);
        rgReportTitle = popReport.findViewById(R.id.rgReportTitle);
        btnSendReport = popReport.findViewById(R.id.btnSendReport);
        btnCancel = popReport.findViewById(R.id.btnCancelReport);

        dialogBuilderReport.setView(popReport);
        dialogReport = dialogBuilderReport.create();
        dialogReport.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogReport.dismiss();
            }
        });
        btnSendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedId = rgReportTitle.getCheckedRadioButtonId();
                if (checkedId == -1) {
                    Toast.makeText(getContext(), "Please Select the report Title", Toast.LENGTH_SHORT).show();
                } else {
                    rbReportTitle = popReport.findViewById(checkedId);
                    Toast.makeText(getContext(), "Reported this video", Toast.LENGTH_SHORT).show();
                    String desc = "";
                    desc = editTextDesc.getText().toString();
                    setReport(rbReportTitle.getText().toString(), desc);
                    dialogReport.dismiss();
                }
            }
        });
    }

    private void setReport(String title, String desc) {
        String uid = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference ref = database.getReference("Users").child(uid).child("Report");
//                                    videoid
        String repId = ref.push().getKey();
        DatabaseReference reportRef = database.getReference("Report").child(repId);
        Map<String, Object> addReport = new HashMap<>();
        addReport.put("Date", getCurrentTimeStamp());
        addReport.put("Video", key);
        addReport.put("Type", title);
        if (!desc.isEmpty() || !desc.equals("")) {
            addReport.put("Description", desc);
        }
        Map<String, Object> addUserReport = new HashMap<>();
        addUserReport.put(repId, "true");
        ref.updateChildren(addUserReport);
        reportRef.updateChildren(addReport);

    }

    private void setReview() {
        final AlertDialog.Builder dialogBuilderReview = new AlertDialog.Builder(getContext());
        final AlertDialog dialogReview;
        final View popReport = getLayoutInflater().inflate(R.layout.pop_window_review, null);
        final Spinner spinner = popReport.findViewById(R.id.spinnerReview);
        Button btnSendReview = popReport.findViewById(R.id.btnSendReview);
        Button btnCancelReview = popReport.findViewById(R.id.btnCancelReview);

        dialogBuilderReview.setView(popReport);
        dialogReview = dialogBuilderReview.create();
        dialogReview.show();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.Review, R.layout.style_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

//        String text=spinner.getSelectedItem().toString();

        btnCancelReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogReview.dismiss();
            }
        });
        btnSendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = spinner.getSelectedItem().toString();
                ratingDA.giveRating(text);
                dialogReview.dismiss();
                Toast.makeText(getContext(), "Thanks Your for providing review.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onStop() {
        super.onStop();
        simpleExoPlayer.stop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        simpleExoPlayer.release();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        simpleExoPlayer.release();
    }
}