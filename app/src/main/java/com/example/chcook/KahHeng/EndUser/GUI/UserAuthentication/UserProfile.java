package com.example.chcook.KahHeng.EndUser.GUI.UserAuthentication;


import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.User;
import com.example.chcook.Domain.Videos;
import com.example.chcook.KahHeng.EndUser.Adapter.UserVideoReviewAdapter;
import com.example.chcook.KahHeng.EndUser.Adapter.VideoAdapter;
import com.example.chcook.KahHeng.EndUser.DA.UserDA;
import com.example.chcook.KahHeng.EndUser.DA.VideoDA;
import com.example.chcook.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfile extends Fragment {
    private View view = null;
    private NavigationView navigationView = null;
    private User user = new User();
    private Uri Imageuri = null;
    private CircleImageView navImageView = null;
    private String name;
    private String email;
    private String uri;
    private CircleImageView imageView;
    private TextView txtName;
    private Button btnCancel=null;
    private UserDA userDA =new UserDA();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public UserProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        setVideoReviewDialog();
        final UserDA userDA = new UserDA();
        txtName = view.findViewById(R.id.editTxtUpdateUserName);
        TextView txtEmail = view.findViewById(R.id.txtUpdateProfileEmail);
        imageView = view.findViewById(R.id.imageView);
        Button btnUpdate = view.findViewById(R.id.btnUpdateProfile);

        navigationView = getActivity().findViewById(R.id.navigationView);
        View header = navigationView.getHeaderView(0);

        final TextView navtxtuUsername = header.findViewById(R.id.username);
        navImageView = header.findViewById(R.id.profile);

        userDA.retrieveUserInfo(new UserDA.UserCallback() {
            @Override
            public User onCallback(User getuser) {

                Glide.with(getContext())
                        .asBitmap()
                        .load(getuser.getImage())
                        .into(imageView);
                txtName.setText(getuser.getName());
                txtEmail.setText(getuser.getEmail());
//                Toast.makeText(MainPage.this, user.getImage(), Toast.LENGTH_SHORT).show();
                return getuser;
            }
        });
//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            name = bundle.getString("Name");
//            email = bundle.getString("Email");
//            uri = bundle.getString("ImageUri");
//
//
//        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageUserImage();
//                user.setImage();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                user.setImage();
                user.setName(txtName.getText().toString());
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

    private void selectImageUserImage() {
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
                upload();
                //set Fragmentclass Arguments

            }

        }
    }

    private String getFileExtension() {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(Imageuri));
    }

    private void upload() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getCurrentUser().getUid();

        if (Imageuri != null) {
            StorageReference imageRef = storageReference.child("/Image/" + uid + "/" + System.currentTimeMillis() + "." + getFileExtension());
            UploadTask uploadTask = imageRef.putFile(Imageuri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    userDA.UpdateUserImage(uri.toString());
                                    Glide.with(getContext())
                                            .asBitmap()
                                            .load(uri)
                                            .into(navImageView);
                                    Glide.with(getContext())
                                            .asBitmap()
                                            .load(uri)
                                            .into(imageView);

                                }

                            });

                        }

                    }
                    Toast.makeText(getActivity(), "User Profile Image Changed", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//                    updateProgress(taskSnapshot);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Select Video and fill in the information", Toast.LENGTH_SHORT).show();
        }
    }
    private void setVideoReviewDialog() {

        final AlertDialog dialogReport;
        final AlertDialog.Builder dialogBuilderReport = new AlertDialog.Builder(getContext());
        final View popReport = getLayoutInflater().inflate(R.layout.pop_window_display_user_review, null);
        btnCancel = popReport.findViewById(R.id.btnCancel);
        recyclerView = popReport.findViewById(R.id.recycleviewReview);
        dialogBuilderReport.setView(popReport);
        dialogReport = dialogBuilderReport.create();
        dialogReport.show();
        VideoDA videoDA=new VideoDA();
        videoDA.getUploadedVideo(videos -> {
//            recyclerView.setHasFixedSize(true);
            adapter = new UserVideoReviewAdapter(popReport.getContext(), videos);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
//            Log.d("test", videos.get(0).getName());
            adapter.notifyDataSetChanged();
//                progressBar.setVisibility(View.GONE);
//                return videos;
            return videos;
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogReport.dismiss();
            }
        });
    }

}
