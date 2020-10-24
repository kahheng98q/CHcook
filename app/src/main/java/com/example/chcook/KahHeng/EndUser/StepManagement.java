package com.example.chcook.KahHeng.EndUser;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.chcook.DA.Adapter;
import com.example.chcook.Domain.Videos;
import com.example.chcook.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepManagement extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;

    public StepManagement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view= inflater.inflate(R.layout.fragment_step_management, container, false);
        ArrayList<Videos> videos =new ArrayList<>();
        recyclerView=view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        adapter=new Adapter(getContext(),videos,"displayRecipe",null);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.manage_menu,menu);
        MenuItem AddVideoItem=menu.findItem(R.id.video);
        MenuItem AddStepItem=menu.findItem(R.id.step);


        super.onCreateOptionsMenu(menu, inflater);
    }

}
