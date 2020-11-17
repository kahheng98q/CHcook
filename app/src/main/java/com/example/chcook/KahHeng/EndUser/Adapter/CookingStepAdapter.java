package com.example.chcook.KahHeng.EndUser.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.CookingSteps;
import com.example.chcook.KahHeng.EndUser.DA.CookingStepDA;
import com.example.chcook.KahHeng.EndUser.DA.FavoriteDA;
import com.example.chcook.KahHeng.EndUser.GUI.VideoAndRecipeManagement.EditCookingStep;
import com.example.chcook.R;

import java.util.ArrayList;


    public class CookingStepAdapter extends RecyclerView.Adapter<CookingStepAdapter.RViewHolder> implements PopupMenu.OnMenuItemClickListener {
        private ArrayList<CookingSteps> cookingSteps;
        private Context context;
        private CookingStepDA cookingStepDA = new CookingStepDA();

        public static class RViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private TextView txtStep;
            private TextView txtDescStep;
            private LinearLayout stepLayout;
            private ImageButton btnMore;


            public RViewHolder(@NonNull View itemView) {
                super(itemView);
//            imageView=itemView.findViewById(R.id.profilePic);
//            textView1=itemView.findViewById(R.id.textViewName);
//            textView2=itemView.findViewById(R.id.txtDate);
//            videoLayout=itemView.findViewById(R.id.homepageLayout);
//
                imageView = itemView.findViewById(R.id.imageViewStep);
                txtStep = itemView.findViewById(R.id.txtStep);
                txtDescStep = itemView.findViewById(R.id.txtStepDesc);
                btnMore = itemView.findViewById(R.id.btnMultiFunc);
                stepLayout = itemView.findViewById(R.id.stepLayout);
            }

        }

        public CookingStepAdapter(Context context, ArrayList<CookingSteps> cookingSteps) {
            this.context = context;
            this.cookingSteps = cookingSteps;
//        Log.d("test","GGGGGGGGGGGGGGGGGGGGGGGGG");
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return false;
        }

        @NonNull
        @Override
        public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cooking_step_cardview, parent, false);
            RViewHolder vh = new RViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull CookingStepAdapter.RViewHolder holder, final int position) {
            final CookingSteps cookingStep = cookingSteps.get(position);
            Log.d("test5",""+position);
            if (!cookingStep.getImageUrl().equals("")) {
                Glide.with(context)
                        .asBitmap()
                        .load(cookingStep.getImageUrl())
                        .into(holder.imageView);
                holder.txtStep.setText("Step"+(position+1));
            }

            holder.txtDescStep.setText(cookingStep.getDescription());
            holder.btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(context, "Removed Video in Histories", Toast.LENGTH_SHORT).show();
                    PopupMenu popupMenu = new PopupMenu(context, v);
                    Log.d("test5",""+position);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {

                                case R.id.itemVDelete:
//                                    Log.d("test5",""+position);
//                                    Log.d("test5",""+cookingSteps.get(position).getDescription());
                                    cookingStepDA.setRecipeKey(cookingSteps.get(position).getRecipes().getRecipeId());
                                    cookingStepDA.setStepKey(cookingSteps.get(position).getStepId());
                                    cookingStepDA.deleteStep();

                                    Toast.makeText(context, "Removed this Step", Toast.LENGTH_SHORT).show();
                                    cookingSteps.remove(position);
                                    notifyDataSetChanged();
                                    return true;
                                case R.id.itemVEdit:
//                                    Log.d("test5",""+position);
//                                    Log.d("test5",""+cookingSteps.get(position).getDescription());
//                                    Log.d("test5",""+cookingSteps.get(position).getStepId());
                                    FragmentManager fragmentManager;
                                    FragmentTransaction fragmentTransaction;
                                    Bundle bundle = new Bundle();
                                    bundle.putString("key",cookingSteps.get(position).getStepId());
                                    bundle.putString("desc", cookingSteps.get(position).getDescription());
                                    bundle.putString("uri",cookingSteps.get(position).getImageUrl());
                                    //set Fragmentclass Arguments
                                    EditCookingStep fragobj = new EditCookingStep();
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
            return cookingSteps.size();
        }
}
