package com.example.chcook.KahHeng.EndUser.Adapter;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Recipes;


import com.example.chcook.KahHeng.EndUser.DA.RecipeDA;
import com.example.chcook.KahHeng.EndUser.GUI.PremiumFeatures.PremiumDisplayCookingStep;
import com.example.chcook.R;
import java.util.ArrayList;
public class PremiumRecipeAdapter extends RecyclerView.Adapter<PremiumRecipeAdapter.RViewHolder> implements PopupMenu.OnMenuItemClickListener{
    private ArrayList<Recipes> recipes;
    private Context context;
    private RecipeDA recipeDA = new RecipeDA();
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
    public static class RViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView txtTitle;
        private TextView txtDesc;
        private ConstraintLayout recipeLayout;
//        private ImageButton btnMore;


        public RViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewRecipePremium);
            txtTitle = itemView.findViewById(R.id.txtRecipeTitlePremium);
            txtDesc = itemView.findViewById(R.id.txtView);

//            btnMore = itemView.findViewById(R.id.btnMultiFunc);
//            btnMore.setVisibility(View.GONE);
            recipeLayout = itemView.findViewById(R.id.recipeInfoLayout);
        }

    }

    public PremiumRecipeAdapter(Context context, ArrayList<Recipes> recipes) {
        this.context = context;
        this.recipes = recipes;

    }

    @NonNull
    @Override
    public PremiumRecipeAdapter.RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_premium_cardview, parent, false);
        RViewHolder vh = new RViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PremiumRecipeAdapter.RViewHolder holder, int position) {
        final Recipes recipe = recipes.get(position);

        if (!recipe.getImageUrl().equals("")) {
            Glide.with(context)
                    .asBitmap()
                    .load(recipe.getImageUrl())
                    .into(holder.imageView);
        }

        holder.txtTitle.setText(recipe.getTitle());
        holder.txtDesc.setText(recipe.getDescription());
        holder.recipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                Toast.makeText(context, recipe.getRecipeId(), Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("title", recipe.getTitle());
                bundle.putString("key", recipe.getRecipeId());

                //set Fragmentclass Arguments
                PremiumDisplayCookingStep fragobj = new PremiumDisplayCookingStep();
                fragobj.setArguments(bundle);
                fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.myNavHostFragment, fragobj);
                fragmentTransaction.addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
