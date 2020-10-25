package com.example.chcook.KahHeng.EndUser;


import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chcook.Domain.CookingSteps;
import com.example.chcook.KahHeng.EndUser.DA.CookingStepDA;
import com.example.chcook.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CookingStepManagement extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String recipeKey;
    private Uri Imageuri;
    private ProgressBar progressBar;

    private Toolbar stepNVideotoolbar;

    public CookingStepManagement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cooking_step_management, container, false);
        stepNVideotoolbar = getActivity().findViewById(R.id.toolbar2);
        stepNVideotoolbar.setVisibility(View.GONE);
        ArrayList<CookingSteps> cookingSteps = new ArrayList<>();
        CookingStepDA cookingStepDA = new CookingStepDA();
        recyclerView = view.findViewById(R.id.recycleview);
        progressBar = view.findViewById(R.id.progressBarRecipeManage);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            recipeKey = bundle.getString("key");


        }


//        cookingStepDA.getUploadedRecipe(new RecipeDA.RecipesCallback() {
//            @Override
//            public ArrayList<Recipes> onCallback(ArrayList<Recipes> recipes) {
//                recyclerView.setHasFixedSize(true);
//                adapter = new RecipeAdapter(getContext(), recipes);
//                layoutManager = new LinearLayoutManager(getActivity());
//                recyclerView.setLayoutManager(layoutManager);
//                recyclerView.setAdapter(adapter);
//
//                adapter.notifyDataSetChanged();
//                progressBar.setVisibility(View.GONE);
//                return recipes;
//            }
//        });

        return view;
    }

}
