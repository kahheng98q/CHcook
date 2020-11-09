package com.example.chcook.YangJie.GUI.Banning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chcook.YangJie.Adapter.*;
import com.example.chcook.Domain.Report;
import com.example.chcook.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Fragment_banUser extends Fragment {

    private ArrayList<Report> reportedUserArrayList = new ArrayList<>();


    private RecyclerView.Adapter adapter;
    private String userStatus,username,position;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Report");


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banuser, container, false);
        Bundle argument = getArguments();
        //get each report
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot report : dataSnapshot.getChildren()) {

                    Long dd = report.child("Date").getValue(Long.class);
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String latestDate = df.format(dd);
                    String Reason = report.child("Description").getValue(String.class);
                    String Reporter = report.child("UserName").getValue(String.class);
                    String videoType = report.child("Type").getValue(String.class);
                    String videoId = report.child("Video").getValue(String.class);
                    reportedUserArrayList.add(new Report(report.getKey(),latestDate,Reason,Reporter,videoId,videoType));
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        if(argument!=null){
            position = argument.getString("position");
//            Toast.makeText(getActivity(),position,Toast.LENGTH_SHORT).show();
        }

        RecyclerView recyclerView = view.findViewById(R.id.banUserRecyclerView);
        adapter = new recylcerViewAdapter_banUser(getContext(), reportedUserArrayList,position);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}
