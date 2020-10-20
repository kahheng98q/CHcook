package com.example.chcook.YangJie.StaffLoginAndManagement;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.chcook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_staffownprofile extends Fragment implements View.OnClickListener {
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    FirebaseAuth fAuth;
    FirebaseDatabase fBase;
    DatabaseReference reference;
    View view;
    private EditText email, name, pass_new, pass_old;
    private CircleImageView image;
    private Button upd, undo;
    private Boolean valid = true;
    private Boolean change = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staffownprofile, container, false);
        email = view.findViewById(R.id.txtProfileEmail);
        name = view.findViewById(R.id.txtProfileName);
        pass_new = view.findViewById(R.id.txtProfilePass_new);
        pass_old = view.findViewById(R.id.txtProfilePass_old);
        image = view.findViewById(R.id.profile_picture);
        upd = view.findViewById(R.id.btnProfileUpdate);
        undo = view.findViewById(R.id.btnUndo);
        fBase = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        upd.setOnClickListener(this);
        undo.setOnClickListener(this);
        image.setOnClickListener(this);
        FirebaseUser staff = FirebaseAuth.getInstance().getCurrentUser();
        email.setText(staff.getEmail());
        name.setText(staff.getDisplayName());
        Glide.with(this)
                .load(staff.getPhotoUrl().toString())
                .into(image);

        return view;
    }

    public void onClick(View v) {
        final FirebaseUser staff = FirebaseAuth.getInstance().getCurrentUser();

        switch (v.getId()) {
            case R.id.btnProfileUpdate:
                AlertDialog.Builder builderR = new AlertDialog.Builder(getActivity());
                builderR.setTitle("Confirm Update");
                builderR.setMessage("Are you sure want to confirm?");
                builderR.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        check(name);
                        check(pass_old);
                        final String passwordOld = pass_old.getText().toString();
                        final String passwordNew = pass_new.getText().toString();
                        if (valid) {
                            AuthCredential credential = EmailAuthProvider.getCredential(staff.getEmail(), passwordOld);
                            staff.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (change) {
                                        updateUserInfo(name.getText().toString(), imageUri, fAuth.getCurrentUser());
                                        if (passwordNew.matches("")) {

                                        } else {
                                            staff.updatePassword(passwordNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(), "changed", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        pass_new.setError("password length must more than 5");
                                                    }
                                                }
                                            });
                                        }
                                    } else {
                                        if (passwordNew.matches("")) {
                                            updateUserInfo(name.getText().toString(), fAuth.getCurrentUser());
                                        } else {
                                            updateUserInfo(name.getText().toString(), fAuth.getCurrentUser());
                                            staff.updatePassword(passwordNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(), "changed", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        pass_new.setError("password length must more than 5");
                                                    }
                                                }
                                            });
                                        }
                                    }


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });
                builderR.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialogR = builderR.create();
                dialogR.show();

                break;
            case R.id.btnUndo:
                Toast.makeText(getActivity(), "undo", Toast.LENGTH_SHORT).show();

                email.setText(staff.getEmail());
                name.setText(staff.getDisplayName());
                Glide.with(this)
                        .load(staff.getPhotoUrl().toString())
                        .into(image);
                pass_new.setText("");
                pass_old.setText("");
                break;
            case R.id.profile_picture:
                Intent gall = new Intent();
                gall.setType("image/*");
                gall.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gall, "Select Profile Pic"), PICK_IMAGE);
                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            try {
                change = true;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean check(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Do not leave empty");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

    private void updateUserInfo(final String sName, Uri imageUri, final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("staff_photos");
        final StorageReference imageFilePath = mStorage.child(imageUri.getLastPathSegment());
        imageFilePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(sName)
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //user info update success

                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Account Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }

                });
            }
        });
    }

    private void updateUserInfo(final String sName, final FirebaseUser currentUser) {

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(sName)
                .build();
        currentUser.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //user info update success

                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Account Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

