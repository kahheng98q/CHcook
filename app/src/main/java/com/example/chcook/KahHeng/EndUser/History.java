package com.example.chcook.KahHeng.EndUser;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chcook.DA.Adapter;
import com.example.chcook.Domain.Videos;
import com.example.chcook.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class History extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;

    public History() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_history, container, false);
        ArrayList<Videos> videos =new ArrayList<>();

        recyclerView=view.findViewById(R.id.HistoryRecyclevView);
        recyclerView.setHasFixedSize(true);
        adapter=new Adapter(getContext(),videos);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        getActivity().setTitle("History");
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.searchbar,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }
}
