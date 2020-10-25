package com.example.chcook.KahHeng.EndUser;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.chcook.Domain.Recipes;
import com.example.chcook.KahHeng.EndUser.DA.Adapter;
import com.example.chcook.Domain.Videos;
import com.example.chcook.KahHeng.EndUser.DA.RecipeAdapter;
import com.example.chcook.KahHeng.EndUser.DA.RecipeDA;
import com.example.chcook.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepManagement extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;
    private Uri Imageuri;
    private ProgressBar progressBar;
    public StepManagement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_step_management, container, false);
        ArrayList<Recipes> recipes = new ArrayList<>();
        RecipeDA recipeDA = new RecipeDA();
        recyclerView = view.findViewById(R.id.recycleview);
        progressBar=view.findViewById(R.id.progressBarRecipeManage);

        recipeDA.getUploadedRecipe(new RecipeDA.RecipesCallback() {
            @Override
            public ArrayList<Recipes> onCallback(ArrayList<Recipes> recipes) {
                recyclerView.setHasFixedSize(true);
                adapter = new RecipeAdapter(getContext(), recipes);
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                return recipes;
            }
        });


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.manage_menu, menu);
//        MenuItem AddVideoItem=menu.findItem(R.id.video);
//        MenuItem AddStepItem=menu.findItem(R.id.step);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.step:

                selectImageRecipe();
                break;
            case R.id.video:
//
//                fragmentManager = getActivity().getSupportFragmentManager();
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.myNavHostFragment, new UploadVideo());
//                fragmentTransaction.commit();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectImageRecipe() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Image"), 10005);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10005) {
            if (resultCode == RESULT_OK && requestCode == 10005 && data != null) {
//                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();

                Imageuri = data.getData();
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;

                Bundle bundle = new Bundle();
                bundle.putString("key", Imageuri.toString());
                //set Fragmentclass Arguments
                UploadRecipe fragobj = new UploadRecipe();
                fragobj.setArguments(bundle);

                fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.myNavHostFragment,new PlayVideo());
                fragmentTransaction.replace(R.id.myNavHostFragment, fragobj);
                fragmentTransaction.commit();
            }

        }
    }
}

