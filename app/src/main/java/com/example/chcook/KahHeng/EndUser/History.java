package com.example.chcook.KahHeng.EndUser;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.chcook.DA.Adapter;
import com.example.chcook.DA.HistoryAdapter;
import com.example.chcook.Domain.Videos;
import com.example.chcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class History extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private ArrayList<Videos> videos;
    private ProgressBar progressBar;
    private ArrayList<String> histories;
//    private Videos video;

    public History() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        videos = new ArrayList<>();
        histories=new ArrayList<>();
        progressBar = view.findViewById(R.id.progressBarHis);

        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("history");

        progressBar.setVisibility(View.VISIBLE);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        getHisDate(child.getKey());
//                        getVideoInform(child.getKey());
                        histories.add(child.getKey());
                    }

                    adapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView = view.findViewById(R.id.HistoryRecyclevView);
        recyclerView.setHasFixedSize(true);

        adapter = new Adapter(getContext(), videos,"history",histories);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        getActivity().setTitle("History");
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.searchbar, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    private void getVideoInform(final Videos HisVideo) {
        DatabaseReference videoRef = database.getReference().child("Videos").child(HisVideo.getVideoID());
        videoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Videos temV=HisVideo;
                    String url = "";
                    String name = "";
                    Log.d("test", "step");

//
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("URL")) {
                            url = child.getValue().toString();
                            temV.setVideo(url);
                        }
                        if (child.getKey().equals("name")) {
                            name = child.getValue().toString();
                            temV.setName(name);
                        }
//                            if (child.getKey().equals("Uploaddate")) {
//                                time = Long.valueOf(child.getValue().toString());
//                                video.setDate(getDate(time));
//                            }
                    }
//                    getHisDate(key);
                    videos.add(temV);
                    progressBar.setVisibility(View.GONE);
//                        videos.add(new Videos(key,name, url, getDate(time)));
                    adapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
        ;

    }

    private String getDate(Long timeStamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timeStamp * 1000);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String date = df.format("dd-MM-yyyy hh:mm", cal).toString();
        return date;
    }

    private void getHisDate(final String key) {

        DatabaseReference Hisref = database.getReference("History").child(key);
        Hisref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Videos video = new Videos();

//                    String date = "";
                    String id = "";
                    Long time = 0L;
                    Log.d("test", "step");

//
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("date")) {
                            time = Long.valueOf(child.getValue().toString());
                            video.setDate(getDate(time));
                        }
                        if (child.getKey().equals("video")) {
                            id = child.getValue().toString();
                            video.setVideoID(id);
                        }
                    }
                    getVideoInform(video);
//                        videos.add(new Videos(key,name, url, getDate(time)));
//                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
