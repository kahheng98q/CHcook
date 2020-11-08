package com.example.chcook.KahHeng.EndUser.GUI.VideoAndRecipeManagement;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chcook.KahHeng.EndUser.DA.RecipeDA;
import com.example.chcook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditRecipe extends Fragment {
    private View view = null;
    private ImageView imageView;
    private EditText nametxt;
    private EditText desctxt;
    private Button editBtn;
    private String key = "";
    private String desc = "";
    private String uri = "";
    private String title = "";
    private RecipeDA recipeDA = new RecipeDA();

    public EditRecipe() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_recipe, container, false);
        imageView = view.findViewById(R.id.editvideoImage);
        nametxt = view.findViewById(R.id.editNameTxt);
        desctxt = view.findViewById(R.id.editDescText);
        editBtn = view.findViewById(R.id.editButton);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            key = bundle.getString("key");
            title = bundle.getString("title");
            uri = bundle.getString("uri");
            desc = bundle.getString("desc");
            Glide.with(getContext())
                    .asBitmap()
                    .load(uri)
                    .into(imageView);

            nametxt.setText(title);
            desctxt.setText(desc);

            recipeDA.setRecipeKey(key);
            editBtn.setOnClickListener(v -> {
                String name = nametxt.getText().toString();
                String desc = desctxt.getText().toString();
                if (!name.isEmpty() && !desc.isEmpty()) {
                    recipeDA.editRecipe(name, desc);
                    Toast.makeText(getContext(), "Edit Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Title and Description cannot be empty", Toast.LENGTH_SHORT).show();
                }

            });
        }

        return view;
    }

}
