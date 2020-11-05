package com.example.chcook.KahHeng.EndUser;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.CookingSteps;
import com.example.chcook.KahHeng.EndUser.DA.CookingStepAdapter;
import com.example.chcook.KahHeng.EndUser.DA.CookingStepDA;
import com.example.chcook.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CookingStepManagement extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String recipeKey = "";
    private String recipeTitle = "";
    private String recipeDesc = "";
    private String recipeImage = "";
    private TextView txtTitle = null;
    private TextView txtDesc = null;
    private ImageView imageViewRecipe = null;
    private Uri Imageuri;
    private ProgressBar progressBar;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
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
        progressBar = getActivity().findViewById(R.id.progressBar);
        txtTitle = view.findViewById(R.id.txtRecipeTitle);
        txtDesc = view.findViewById(R.id.txtRecipeDesc);
        imageViewRecipe = view.findViewById(R.id.imageViewRecipeInCookM);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            recipeKey = bundle.getString("key");
            recipeTitle = bundle.getString("title");
            recipeDesc = bundle.getString("desc");
            recipeImage = bundle.getString("uri");
            Glide.with(getContext())
                    .asBitmap()
                    .load(recipeImage)
                    .into(imageViewRecipe);
            txtDesc.setText(recipeDesc);
//            Toast.makeText(getActivity(), recipeDesc, Toast.LENGTH_SHORT).show();
            txtTitle.setText(recipeTitle);
            cookingStepDA.setRecipeKey(recipeKey);
        }


        cookingStepDA.getUploadedCooking(new CookingStepDA.StepsCallback() {
            @Override
            public ArrayList<CookingSteps> onCallback(ArrayList<CookingSteps> cookingSteps) {
                recyclerView.setHasFixedSize(true);
                adapter = new CookingStepAdapter(getContext(), cookingSteps);
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                return cookingSteps;

            }
        });

        getActivity().setTitle("Recipe Procedure");
        return view;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.step_manage_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemAddStep:
                selectImageStep();
                break;
            case R.id.video:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectImageStep() {
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
                bundle.putString("Recipekey", recipeKey);
//                Toast.makeText(getActivity(), recipeKey, Toast.LENGTH_SHORT).show();
                //set Fragmentclass Arguments
                UploadCookingStep fragobj = new UploadCookingStep();
                fragobj.setArguments(bundle);

                fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.myNavHostFragment, fragobj);
                fragmentTransaction.addToBackStack(null).commit();
            }

        }
    }
}

