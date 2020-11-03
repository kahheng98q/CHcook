package com.example.chcook.YangJie;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import com.example.chcook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_addStaff extends Fragment implements View.OnClickListener {
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    FirebaseAuth fAuth;
    FirebaseDatabase fBase;
    DatabaseReference reference;
    private CircleImageView profilePicStaff;
    private Button register, clean;
    private EditText Email, Name, Pass, CPass;
    private TextView path;
    private String ff;
    private String lastURL;
    private ProgressBar pg;
    private Boolean valid = true;
    private RelativeLayout pgb;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addstaff, container, false);
        profilePicStaff = (CircleImageView) view.findViewById(R.id.add_img);
        register = view.findViewById(R.id.btnRegAdd);
        pg = view.findViewById(R.id.progressBarAddStaff);
        pgb = view.findViewById(R.id.progressBarAddStaffB);
        clean = view.findViewById(R.id.btnClearAdd);
        Email = view.findViewById(R.id.staffLoginEmail);
        Name = view.findViewById(R.id.addName);
        Pass = view.findViewById(R.id.addPass);
        path = view.findViewById(R.id.addPath);
        CPass = view.findViewById(R.id.addConfirm);
        fBase = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        profilePicStaff.setOnClickListener(this);
        register.setOnClickListener(this);
        clean.setOnClickListener(this);
        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                profilePicStaff.setImageBitmap(bitmap);
//                Toast.makeText(getActivity(), imageUri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()) {
            //get profile image
            case R.id.add_img:
                Intent gall = new Intent();
                gall.setType("image/*");
                gall.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gall, "Select Profile Pic"), PICK_IMAGE);

                break;
            // register new staff
            case R.id.btnRegAdd:
                AlertDialog.Builder builderR = new AlertDialog.Builder(getActivity());
                builderR.setTitle("Confirm Registration");
                builderR.setMessage("Are you sure want to confirm?");
                builderR.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkImg(imageUri);
                        check(Email);
                        checkEmail(Email);
                        check(Name);
                        checkPass(Pass);
                        check(Pass);
                        check(CPass);
                        String password = Pass.getText().toString();
                        if (valid) {
                            if (!password.equals(CPass.getText().toString())) {
                                Pass.setError("Password not match!");
                            } else {
                                pg.setVisibility(View.VISIBLE);
                                pgb.setVisibility(View.VISIBLE);
                                final String sName = Name.getText().toString();
                                final String sEmail = Email.getText().toString();
                                final String sPass = Pass.getText().toString();
                                final String sStatus = "Working";
                                final Boolean IsAdmin = false;
                                fAuth.createUserWithEmailAndPassword(sEmail, sPass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        final String id = authResult.getUser().getUid();
//                                        Toast.makeText(getActivity(), "new staff account created", Toast.LENGTH_SHORT).show();
                                        final FirebaseUser user = fAuth.getCurrentUser();
                                        reference = fBase.getReference("Staff").child(user.getUid());
//                                        Staff  st = new Staff(sEmail,sName,sPass,IsAdmin,sStatus);
//                                        updateUserInfo(sName,imageUri,fAuth.getCurrentUser());


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
                                                        ff = uri.toString();
                                                        user.updateProfile(profileUpdate)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        //user info update success
                                                                        if (task.isSuccessful()) {
                                                                            fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        Map<String, Object> staffInfo = new HashMap<>();
                                                                                        staffInfo.put("StaffEmail", sEmail);
                                                                                        staffInfo.put("StaffName", sName);
//                                                                                        staffInfo.put("StaffPassword", sPass);
                                                                                        staffInfo.put("StaffStatus", sStatus);
                                                                                        staffInfo.put("IsAdmin", IsAdmin);
                                                                                        staffInfo.put("ProfileImage",ff);
                                                                                        staffInfo.put("StaffId",id);
                                                                                        reference.setValue(staffInfo);
                                                                                        Toast.makeText(v.getContext(),"Register successfully, please check mailbox for verification",Toast.LENGTH_SHORT).show();
                                                                                    }else{
                                                                                        Toast.makeText(v.getContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                                                                                    }
                                                                                    pg.setVisibility(View.GONE);
                                                                                    pgb.setVisibility(View.GONE);
                                                                                }
                                                                            });


                                                                        }
                                                                    }
                                                                });

                                                    }

                                                });
                                            }
                                        });


                                        Email.setText("");
                                        Name.setText("");
                                        Pass.setText("");
                                        CPass.setText("");

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pg.setVisibility(View.GONE);
                                        pgb.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
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
            //clear all text
            case R.id.btnClearAdd:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Clear");
                builder.setMessage("Are you sure want to clear?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Email.setText("");
                        Name.setText("");
                        Pass.setText("");
                        CPass.setText("");
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }

    }

    private boolean checkPass(EditText pass) {
        if(pass.getText().length()<6){
            valid = false;
            pass.setError("Password length should more than 6");

        }else{
            valid = true;
            pass.setError(null);
        }
        return valid;
    }

    private boolean checkImg(Uri imageUri) {
        if(imageUri!=null){
            valid = true;

        }else{
            valid = false;
            Toast.makeText(getContext(),"Please select profile image",Toast.LENGTH_SHORT).show();
        }
        return valid;
    }
    private  boolean checkEmail(EditText textField) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(textField.getText().toString());
        if(matcher.matches()!=true){
            Email.setError("Please enter a valid email");
            valid = false;
        }else{
            valid = true;
        }
        return valid;
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
}
