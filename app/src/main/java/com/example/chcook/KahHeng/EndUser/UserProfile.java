package com.example.chcook.KahHeng.EndUser;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.User;
import com.example.chcook.KahHeng.EndUser.DA.UserDA;
import com.example.chcook.R;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfile extends Fragment {
    private View view = null;
    private NavigationView navigationView = null;
    private User user=new User();
    public UserProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        final UserDA userDA = new UserDA();

        TextView txtName = view.findViewById(R.id.editTxtUpdateUserName);
        CircleImageView imageView = view.findViewById(R.id.imageView);
        Button btnUpdate= view.findViewById(R.id.btnUpdateProfile);

        navigationView = getActivity().findViewById(R.id.navigationView);
        View header = navigationView.getHeaderView(0);

        final TextView navtxtuUsername = header.findViewById(R.id.username);
        final CircleImageView navImageView = header.findViewById(R.id.profile);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                user.setImage();
//                user.setName();
                userDA.UpdateUser(user);
                Glide.with(getContext())
                        .asBitmap()
                        .load(user.getImage())
                        .into(navImageView);


                navtxtuUsername.setText(user.getName());
            }
        });


        return view;
    }

}
