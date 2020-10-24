package com.example.chcook.KahHeng.EndUser;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.chcook.DA.Adapter;
import com.example.chcook.Domain.Videos;
import com.example.chcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import kotlin.jvm.internal.LocalVariableReference;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoManagement extends Fragment {
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

    public VideoManagement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_video_management, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        videos = new ArrayList<>();
        progressBar=view.findViewById(R.id.progressBarVideoManage);
//        videos.add(new Videos("1","Fried Rice","3K","01/08/2020","https://firebasestorage.googleapis.com/v0/b/chcook-30453.appspot.com/o/recipe%2FChicken%20rice.PNG?alt=media&token=d21f8eed-8c2a-4cd7-84a0-e022f5e2facc","Heng"));
//        videos.add(new Videos("2","Chicken Rice","3K","02/08/2020","https://firebasestorage.googleapis.com/v0/b/chcook-30453.appspot.com/o/recipe%2Ffried%20rice.PNG?alt=media&token=305ca511-81ed-4f56-a931-83a481a9aada","Heng"));
//        videos.add(new Videos("3","Fried noodle","3K","03/08/2020","https://firebasestorage.googleapis.com/v0/b/chcook-30453.appspot.com/o/recipe%2FSpagetti.PNG?alt=media&token=f8ec1058-0478-4594-bb56-0921cbc6dff8","Heng"));
//        videos.add(new Videos("4","Mee Sedap","3K","04/08/2020","https://firebasestorage.googleapis.com/v0/b/chcook-30453.appspot.com/o/recipe%2Fmee.PNG?alt=media&token=75394ace-33e3-41c6-9a52-5f78bbed65dd","Heng"));
//        videos.add(new Videos("5","Salt chicken","3K","05/08/2020","https://firebasestorage.googleapis.com/v0/b/chcook-30453.appspot.com/o/recipe%2FSalt%20chicken.PNG?alt=media&token=e16af538-c3fe-4fb8-aedd-d3871ff632ec","Heng"));
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("video");
//                DatabaseReference ref = database.getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
        progressBar.setVisibility(View.VISIBLE);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        getVideoInform(child.getKey());

                    }

                    adapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerView = view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        adapter = new Adapter(getContext(), videos,"video",null);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void getVideoInform(final String key) {
        DatabaseReference videoRef = database.getReference().child("Videos").child(key);
        videoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String url = "";
                    String name = "";
                    Long time = 0L;
                    Log.d("test", "step");
                    if (dataSnapshot.exists()) {

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.getKey().equals("URL")) {
                                url = child.getValue().toString();
                            }
                            if (child.getKey().equals("name")) {
                                name = child.getValue().toString();
                            }
                            if (child.getKey().equals("Uploaddate")) {
                                time = Long.valueOf(child.getValue().toString());

                            }
                        }
                        videos.add(new Videos(key,name, url, getDate(time)));
                        adapter.notifyDataSetChanged();
                    }

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
        ;
//                getVideoInform();

    }

    private String getDate(Long timeStamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timeStamp * 1000);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String date = df.format("dd-MM-yyyy hh:mm", cal).toString();
        return date;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.manage_menu, menu);


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        MenuItem AddVideoItem=menu.findItem(R.id.video);
//        MenuItem AddStepItem=menu.findItem(R.id.step);
        switch (item.getItemId()) {
            case R.id.step:
                Log.d("test", "step");
                break;
            case R.id.video:

                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.myNavHostFragment, new UploadVideo());
                fragmentTransaction.commit();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
