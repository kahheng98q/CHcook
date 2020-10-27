package com.example.chcook.KahHeng.EndUser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.chcook.Domain.Recipes;
import com.example.chcook.KahHeng.EndUser.DA.Adapter;
import com.example.chcook.Domain.Videos;
import com.example.chcook.KahHeng.EndUser.DA.RecipeAdapter;
import com.example.chcook.KahHeng.EndUser.DA.RecipeDA;
import com.example.chcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DisplayRecipes extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private ArrayList<Videos> videos;
    private ProgressBar progressBar;
    private RecipeDA recipeDA=new RecipeDA();
    public DisplayRecipes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_display_recipes, container, false);

        recyclerView=view.findViewById(R.id.RecipeRecyclevView);
        progressBar=getActivity().findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        recipeDA.getAllRecipesInPremium(new RecipeDA.RecipesCallback() {
            @Override
            public ArrayList<Recipes> onCallback(ArrayList<Recipes> recipes) {
                adapter = new RecipeAdapter(getContext(), recipes);
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);
                return recipes;
            }
        });
        recyclerView.setHasFixedSize(true);

        getActivity().setTitle("Recipe");
        return view;
    }


}
