package com.example.chcook.YangJie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;

public class Fragment_banvideo extends Fragment {

    private ArrayList<Report> reportedVideoArraylist = new ArrayList<>();
    private RecyclerView.Adapter adapter;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Report");

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_banvideo, container, false);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot report : dataSnapshot.getChildren()) {

                    String dd = report.child("ReportDate").getValue(String.class);
                    String Reason = report.child("Reason").getValue(String.class);
                    String Reporter = report.child("UserName").getValue(String.class);
                    String videoName = report.child("VideoName").getValue(String.class);
                    String videoId = report.child("VideoId").getValue(String.class);

                    reportedVideoArraylist.add(new Report(report.getKey(),dd, Reason, Reporter, videoId, videoName));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.banVideoRecyclerView);
        adapter = new recyclerViewAdapter_banVideo(getContext(), reportedVideoArraylist);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

}
