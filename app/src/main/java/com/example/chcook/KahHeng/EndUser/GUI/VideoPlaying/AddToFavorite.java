package com.example.chcook.KahHeng.EndUser.GUI.VideoPlaying;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.chcook.KahHeng.EndUser.Adapter.FavoriteAdapter;
import com.example.chcook.KahHeng.EndUser.DA.FavoriteDA;
import com.example.chcook.Domain.Favorite;
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
    private ProgressBar progressBar;
    //    private ArrayList<String> favList;
    private ArrayList<Favorite> tmpfavorites;
    private FavoriteDA favoriteDA = new FavoriteDA();

    public AddToFavorite() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_to_favorite, container, false);

        tmpfavorites=new ArrayList<>();
//        ArrayList<Videos> favorites = new ArrayList<>();

        progressBar = view.findViewById(R.id.progressBarFav);
        recyclerView = view.findViewById(R.id.favoriteRecycleView);
        favoriteDA.retrieveFavorite(new FavoriteDA.CallFavorite() {
            @Override
            public ArrayList<Favorite> onCallback(ArrayList<Favorite> favorites) {
                tmpfavorites=favorites;
//                Log.d("test", "favstep");
                recyclerView.setHasFixedSize(true);
                adapter = new FavoriteAdapter(getContext(), tmpfavorites);
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                return favorites;
            }
        });


        getActivity().setTitle("Favorite List");
        return view;
    }

}
