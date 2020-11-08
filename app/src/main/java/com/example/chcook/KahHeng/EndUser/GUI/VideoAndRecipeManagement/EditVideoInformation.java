package com.example.chcook.KahHeng.EndUser.GUI.VideoAndRecipeManagement;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Videos;
import com.example.chcook.KahHeng.EndUser.DA.VideoDA;
import com.example.chcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditVideoInformation extends Fragment {
    private View view = null;
    private ImageView imageView;
    private EditText nametxt;
    private EditText desctxt;
    private Button editBtn;
    private String key = "";
    private VideoDA videoDA = new VideoDA();
    private Spinner spinner = null;

    public EditVideoInformation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_video_information, container, false);
        imageView = view.findViewById(R.id.editvideoImage);
        nametxt = view.findViewById(R.id.editNameTxt);
        desctxt = view.findViewById(R.id.editDescText);
        editBtn = view.findViewById(R.id.editButton);
        spinner = view.findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.Categories, R.layout.style_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
//        spinner.setSelection(0);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            key = bundle.getString("key");
            videoDA.setVideokey(key);
            videoDA.getVideoInform(new VideoDA.Callvideo() {
                @Override
                public Videos onCallVideo(Videos video) {

                    Glide.with(getContext())
                            .asBitmap()
                            .load(video.getVideo())
                            .into(imageView);

                    nametxt.setText(video.getName());
                    desctxt.setText(video.getDesc());
                    setSpinText(video.getCategory());
                    return video;
                }
            });
        }


        editBtn.setOnClickListener(v -> {
            String name = nametxt.getText().toString();
            String desc = desctxt.getText().toString();
            String cate = spinner.getSelectedItem().toString();
            if (!name.isEmpty() && !desc.isEmpty()) {
                videoDA.editVideoNameNDesc(name, desc, cate);

                Toast.makeText(getContext(), "Edit Successful", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getApplicationContext(), MainPage.class));

            } else {
                Toast.makeText(getContext(), "Name and Description can be empty", Toast.LENGTH_SHORT).show();
            }

        });
        return view;
    }

    private void setSpinText(String text) {
        for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
            if (spinner.getAdapter().getItem(i).toString().contains(text)) {
                spinner.setSelection(i);
            }
        }

    }
}
