package com.example.chcook;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chcook.DA.Adapter;
import com.example.chcook.Domain.Videos;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoManagement extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;
    public VideoManagement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_video_management, container, false);

        ArrayList<Videos> videos =new ArrayList<>();
        videos.add(new Videos("1","Fried Rice","3K","01/08/2020","https://firebasestorage.googleapis.com/v0/b/chcook-30453.appspot.com/o/recipe%2FChicken%20rice.PNG?alt=media&token=d21f8eed-8c2a-4cd7-84a0-e022f5e2facc","Heng"));
        videos.add(new Videos("2","Chicken Rice","3K","02/08/2020","https://firebasestorage.googleapis.com/v0/b/chcook-30453.appspot.com/o/recipe%2Ffried%20rice.PNG?alt=media&token=305ca511-81ed-4f56-a931-83a481a9aada","Heng"));
        videos.add(new Videos("3","Fried noodle","3K","03/08/2020","https://firebasestorage.googleapis.com/v0/b/chcook-30453.appspot.com/o/recipe%2FSpagetti.PNG?alt=media&token=f8ec1058-0478-4594-bb56-0921cbc6dff8","Heng"));
        videos.add(new Videos("4","Mee Sedap","3K","04/08/2020","https://firebasestorage.googleapis.com/v0/b/chcook-30453.appspot.com/o/recipe%2Fmee.PNG?alt=media&token=75394ace-33e3-41c6-9a52-5f78bbed65dd","Heng"));
        videos.add(new Videos("5","Salt chicken","3K","05/08/2020","https://firebasestorage.googleapis.com/v0/b/chcook-30453.appspot.com/o/recipe%2FSalt%20chicken.PNG?alt=media&token=e16af538-c3fe-4fb8-aedd-d3871ff632ec","Heng"));

        recyclerView=view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        adapter=new Adapter(getContext(),videos);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

}
