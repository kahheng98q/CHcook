package com.example.chcook.KahHeng.EndUser.GUI.VideoAndRecipeManagement;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.chcook.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowCookingStep extends Fragment {
    private ImageView imageView;
    private View view;
    private String head;
    public ShowCookingStep() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_show_cooking_step, container, false);
        imageView=view.findViewById(R.id.CookingStepImage);
        getActivity().setTitle("Cooking Step");
        head="https://firebasestorage.googleapis.com/v0/b/chcook-30453.appspot.com/o/recipe%2FChicken%20rice.PNG?alt=media&token=d21f8eed-8c2a-4cd7-84a0-e022f5e2facc";
        Glide.with(getContext())
                .asBitmap()
                .load(head)
                .into(imageView);

        return view;
    }

}
