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
import com.example.chcook.KahHeng.EndUser.DA.CookingStepDA;
import com.example.chcook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditCookingStep extends Fragment {
    private View view = null;
    private ImageView imageView;
    private EditText desctxt;
    private Button editBtn;
    private String key = "";
    private String desc = "";
    private String uri = "";
    private CookingStepDA cookingStepDA = new CookingStepDA();

    public EditCookingStep() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_edit_cooking_step, container, false);
        imageView = view.findViewById(R.id.editvideoImage);
        desctxt = view.findViewById(R.id.editDescText);
        editBtn = view.findViewById(R.id.editButton);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            key = bundle.getString("key");
            key = bundle.getString("key");
            uri = bundle.getString("uri");
            desc = bundle.getString("desc");
            Glide.with(getContext())
                    .asBitmap()
                    .load(uri)
                    .into(imageView);
            desctxt.setText(desc);
        }
        editBtn.setOnClickListener(v -> {
            String desc=desctxt.getText().toString();
            cookingStepDA.setStepKey(key);
            if(!desc.isEmpty()){
                cookingStepDA.editCookingStep(desc);

                Toast.makeText(getContext(),"Edit Successful" , Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), MainPage.class));

            }else {
                Toast.makeText(getContext(),"Description cannot be empty" , Toast.LENGTH_SHORT).show();
            }

        });
        return view;
    }

}
