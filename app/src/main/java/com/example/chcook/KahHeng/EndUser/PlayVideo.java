package com.example.chcook.KahHeng.EndUser;


import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.media.Rating;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayVideo extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private View view;

    private VideoView imageView;
    private TextView txtRate;
    private TextView txtView;
    private TextView txtDate;
    private TextView txtName;
    private TextView txtdesc;
    private ImageView userPro;
    private TextView txtUser;
    private ImageButton btnFav;
    private ImageButton btnReport;
    private String url;
    private String head;
    private String key;
    private ArrayList<Videos> videos = new ArrayList<>();
    private MediaController mc;
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
    private RatingDA ratingDA=new RatingDA();
    private ArrayList<Ratings> ratings=new ArrayList<>();
    private double givenRate=0;
    private UserDA userDA=new UserDA();
    public PlayVideo() {
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

        imageView = view.findViewById(R.id.videoImage);
        txtView = view.findViewById(R.id.txtView);
        txtDate = view.findViewById(R.id.txtDate);
        txtName = view.findViewById(R.id.txtName);

        txtUser = view.findViewById(R.id.txtUsername);
        txtdesc = view.findViewById(R.id.txtDesc);
        userPro = view.findViewById(R.id.userHead);

        btnFav = view.findViewById(R.id.btnFavorite);
        btnReport = view.findViewById(R.id.btnReport);

        txtRate=view.findViewById(R.id.txtRate);
        progressBar = view.findViewById(R.id.progressBarPlay);
        progressBar.setVisibility(View.VISIBLE);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            key = bundle.getString("key");
            ratingDA.setVideoKey(key);
            ratingDA.getAvgRating(new RatingDA.RatingCallback() {
                @Override
                public void onCallback(ArrayList<Ratings> retrievedRatings, double rating, int numofRate) {
                    ratings=retrievedRatings;
                    txtRate.setText(String.format("%.1f", rating));
                    ratingDA.CheckRating(new RatingDA.onCheckRate() {
                        @Override
                        public void onCallback(double ratingGiven) {
                            givenRate=ratingGiven;
                            Toast.makeText(getContext(), ""+givenRate, Toast.LENGTH_SHORT).show();
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
                imageView.setVideoURI(Uri.parse(video.getVideo()));
                txtDate.setText(video.getDate());
                txtView.setText(video.getView());
                return video;
            }
        });

//        setUser(key);
       userDA. setVideokey(key);
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

        imageView.start();
        imageView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mc = new MediaController(getActivity());
                        imageView.setMediaController(mc);
                        mc.setAnchorView(imageView);
                        progressBar.setVisibility(View.GONE);
                        mp.start();
                    }
                });
            }
        });
        imageView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                    progressBar.setVisibility(View.GONE);
                }
                if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                    progressBar.setVisibility(View.GONE);
                }
                return false;
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
                if (givenRate>=1){
                    Toast.makeText(getContext(), "This video's rating has been given.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), ""+givenRate, Toast.LENGTH_SHORT).show();
                    setReview();
                }

            }
        });
        getActivity().setTitle("Play Video");
        return view;
    }

    //Favorite
    private void setFavorite(String videoKey) {
        String uid = firebaseAuth.getCurrentUser().getUid();
        final DatabaseReference userRef = database.getReference().child("Users").child(uid).child("Favorite");

        String favId = userRef.push().getKey();
        DatabaseReference favRef = database.getReference("Favorite").child(favId);

        Map<String, Object> addFav = new HashMap<>();
        addFav.put("Video", videoKey);
        addFav.put("Date", getCurrentTimeStamp());

        Map<String, Object> addFavID = new HashMap<>();
        addFavID.put(favId, true);
        pfavKey = favId;
        favRef.updateChildren(addFav);
        userRef.updateChildren(addFavID);
        fav = true;

        Toast.makeText(getContext(), "Added Video in Favorite List", Toast.LENGTH_SHORT).show();
    }

    // Date Function
    private String getDate(Long timeStamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timeStamp * 1000);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String date = df.format("dd-MM-yyyy hh:mm", cal).toString();
        return date;
    }

    private long getCurrentTimeStamp() {
        long timestamp = System.currentTimeMillis() / 1000;
        return timestamp;
    }

    //Histories



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
        spinner.setSelection(4);

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
                String text=spinner.getSelectedItem().toString();
                ratingDA.giveRating(text);
//                int checkedId=rgReportTitle.getCheckedRadioButtonId();

//                    rbReportTitle=popReport.findViewById(checkedId);
//                    Toast.makeText(getContext(),"Reported this video" , Toast.LENGTH_SHORT).show();
//                    String desc="";
//                    desc=editTextDesc.getText().toString();
//                    setReport(rbReportTitle.getText().toString(),desc);
                
                dialogReview.dismiss();

            }
        });
    }
}