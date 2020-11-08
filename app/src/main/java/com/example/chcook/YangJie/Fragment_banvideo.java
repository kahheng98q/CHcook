package com.example.chcook.YangJie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.chcook.YangJie.DA.recyclerViewAdapter_banVideo;
import com.example.chcook.Domain.Report;
import com.example.chcook.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Fragment_banvideo extends Fragment {

    private ArrayList<Report> reportedVideoArraylist = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private String position;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Report");

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_banvideo, container, false);
        Bundle argument = getArguments();

//        String position = argument.getString("position");
//        Toast.makeText(getActivity(), position, Toast.LENGTH_SHORT).show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    for (DataSnapshot report : dataSnapshot.getChildren()) {

                        Long dd = report.child("Date").getValue(Long.class);
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        String latestDate = df.format(dd);
                        String Reason = report.child("Description").getValue(String.class);
//                        String Reporter = report.child("UserName").getValue(String.class);
//                        String videoName = report.child("VideoName").getValue(String.class);
                        String videoId = report.child("Video").getValue(String.class);
                        String videoType = report.child("Type").getValue(String.class);

                        reportedVideoArraylist.add(new Report(report.getKey(), latestDate, Reason, videoId,videoType));
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.banVideoRecyclerView);
        if(argument!=null){
            position = argument.getString("position");
        }
        adapter = new recyclerViewAdapter_banVideo(getContext(), reportedVideoArraylist,position);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

}
