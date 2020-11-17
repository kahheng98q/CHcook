package com.example.chcook.KahHeng.EndUser.Adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Recipes;


import com.example.chcook.KahHeng.EndUser.GUI.VideoAndRecipeManagement.CookingStepManagement;
import com.example.chcook.KahHeng.EndUser.DA.RecipeDA;
import com.example.chcook.KahHeng.EndUser.GUI.VideoAndRecipeManagement.EditRecipe;
import com.example.chcook.KahHeng.EndUser.GUI.VideoAndRecipeManagement.EditVideoInformation;
import com.example.chcook.R;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RViewHolder> implements PopupMenu.OnMenuItemClickListener {
    private ArrayList<Recipes> recipes;
    private Context context;
    private RecipeDA recipeDA = new RecipeDA();


    public static class RViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView1;
        private TextView textView2;
        private LinearLayout videoInfoLayout;
        private ImageButton btnMore;


        public RViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profilePic);
            textView1 = itemView.findViewById(R.id.textViewName);
            textView2 = itemView.findViewById(R.id.textViewAddress);
            btnMore = itemView.findViewById(R.id.btnMultiFunc);
            videoInfoLayout = itemView.findViewById(R.id.videoinfoLayout);
        }

    }

    public RecipeAdapter(Context context, ArrayList<Recipes> recipes) {
        this.context = context;
        this.recipes = recipes;

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @NonNull
    @Override
    public RecipeAdapter.RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewcontent, parent, false);
        RViewHolder vh = new RViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RViewHolder holder, final int position) {
        final Recipes recipe = recipes.get(position);

        if (!recipe.getImageUrl().equals("")) {
            Glide.with(context)
                    .asBitmap()
                    .load(recipe.getImageUrl())
                    .into(holder.imageView);
        }

        holder.textView1.setText(recipe.getTitle());
        holder.textView2.setText(recipe.getUploadDate());
        holder.videoInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                Bundle bundle = new Bundle();
                bundle.putString("key", recipe.getRecipeId());
                bundle.putString("title", recipe.getTitle());
                bundle.putString("desc", recipe.getDescription());
                bundle.putString("uri", recipe.getImageUrl());
                //set Fragmentclass Arguments
                CookingStepManagement fragobj = new CookingStepManagement();
                fragobj.setArguments(bundle);
                fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.myNavHostFragment, fragobj);
                fragmentTransaction.addToBackStack(null).commit();

            }
        });
        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "Removed Video in Histories", Toast.LENGTH_SHORT).show();
                PopupMenu popupMenu = new PopupMenu(context, v);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.itemVDelete:
                                recipeDA.setRecipeKey(recipes.get(position).getRecipeId());
                                recipeDA.deleteRecipe();
                                Toast.makeText(context, "Deleted the Recipe", Toast.LENGTH_SHORT).show();
                                recipes.remove(position);
                                notifyDataSetChanged();
                                return true;

                            case R.id.itemVEdit:
                                Log.d("test5",""+position);
                                FragmentManager fragmentManager;
                                FragmentTransaction fragmentTransaction;
                                Bundle bundle = new Bundle();
                                bundle.putString("key", recipe.getRecipeId());
                                bundle.putString("title", recipe.getTitle());
                                bundle.putString("desc", recipe.getDescription());
                                bundle.putString("uri", recipe.getImageUrl());
                                //set Fragmentclass Arguments
                                EditRecipe fragobj = new EditRecipe();
                                fragobj.setArguments(bundle);
                                fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.myNavHostFragment, fragobj);
                                fragmentTransaction.addToBackStack(null).commit();
                                return true;

                            default:
                                return false;
                        }

                    }
                });
                popupMenu.inflate(R.menu.video_manage_menu);
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
