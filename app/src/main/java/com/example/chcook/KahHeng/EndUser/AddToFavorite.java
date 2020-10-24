package com.example.chcook.KahHeng.EndUser;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chcook.KahHeng.EndUser.DA.Adapter;
import com.example.chcook.KahHeng.EndUser.Domain.Videos;
import com.example.chcook.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddToFavorite extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;
    private ArrayList<String> favList;
    public AddToFavorite() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_add_to_favorite, container, false);
        ArrayList<Videos> videos =new ArrayList<>();
        favList=new ArrayList<>();
        recyclerView=view.findViewById(R.id.favoriteRecycleView);
        recyclerView.setHasFixedSize(true);
        adapter=new Adapter(getContext(),videos,"favorite",favList);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        getActivity().setTitle("Favorite List");
        return view;
    }

}
