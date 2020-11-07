package com.example.chcook.KahHeng.EndUser.GUI.VideoAndRecipeManagement;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.chcook.KahHeng.EndUser.Adapter.VideoAdapter;
import com.example.chcook.Domain.Videos;
import com.example.chcook.KahHeng.EndUser.DA.VideoDA;
import com.example.chcook.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


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
    private Uri uri = null;
    private VideoDA videoDA = new VideoDA();
    private TabLayout tabLayout=null;

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
        progressBar = view.findViewById(R.id.progressBarVideoManage);

        database = FirebaseDatabase.getInstance();

        recyclerView = view.findViewById(R.id.recycleview);
        progressBar.setVisibility(View.VISIBLE);

        videoDA.getUploadedVideo(new VideoDA.VideoCallback() {
            @Override
            public ArrayList<Videos> onCallback(ArrayList<Videos> videos) {
                recyclerView.setHasFixedSize(true);
                adapter = new VideoAdapter(getContext(), videos);
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                return videos;
            }
        });
        settablayout();
        return view;
    }


    public void settablayout() {
        tabLayout=view.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (! tab.getText().toString().toUpperCase().equals("VIDEO")) {
//                    fragmentManager = ((FragmentActivity)getContext()).getSupportFragmentManager();
//                    fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.myNavHostFragment, new VideoManagement());
//                    fragmentTransaction.addToBackStack(null).commit();
                    fragmentManager = ((FragmentActivity)getContext()).getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.myNavHostFragment, new RecipeManagementUI());
                    fragmentTransaction.addToBackStack(null).commit();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
        switch (item.getItemId()) {
            case R.id.step:
                selectImageRecipe();
                break;
            case R.id.video:
                selectVideo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectImageRecipe() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Image"), 10002);
    }

    private void selectVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video"), 10005);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10002) {
            if (resultCode == RESULT_OK && requestCode == 10002 && data != null) {
//                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();

                uri = data.getData();
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;

                Bundle bundle = new Bundle();
                bundle.putString("key", uri.toString());
                //set Fragmentclass Arguments
                UploadRecipe fragobj = new UploadRecipe();
                fragobj.setArguments(bundle);

                fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.myNavHostFragment, fragobj);
                fragmentTransaction.commit();
            }
        } else if (resultCode == RESULT_OK && requestCode == 10005 && data != null) {
            uri = data.getData();
            FragmentManager fragmentManager;
            FragmentTransaction fragmentTransaction;

            Bundle bundle = new Bundle();
            bundle.putString("key", uri.toString());
            //set Fragmentclass Arguments
            UploadVideo fragobj = new UploadVideo();
            fragobj.setArguments(bundle);

            fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.myNavHostFragment, fragobj);
            fragmentTransaction.commit();
        }
    }
}
