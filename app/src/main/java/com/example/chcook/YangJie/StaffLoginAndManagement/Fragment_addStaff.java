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
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import com.example.chcook.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_addStaff extends Fragment implements View.OnClickListener {
    private CircleImageView profilePicStaff;
    private Button register, clean;
    private EditText Email, Name, Pass, CPass;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    private Boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseDatabase fBase;
    DatabaseReference reference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addstaff, container, false);
        profilePicStaff = (CircleImageView) view.findViewById(R.id.profilePic_staff);
        register = view.findViewById(R.id.btnRegister);
        clean = view.findViewById(R.id.btnClear);
        Email = view.findViewById(R.id.txtEmail);
        Name = view.findViewById(R.id.txtName);
        Pass = view.findViewById(R.id.txtPass);
        CPass = view.findViewById(R.id.txtCfmPass);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.profilePic_staff:
                Intent gall = new Intent();
                gall.setType("image/*");
                gall.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gall, "Select Profile Pic"), PICK_IMAGE);
                break;
            case R.id.btnRegister:
                AlertDialog.Builder builderR = new AlertDialog.Builder(getActivity());
                builderR.setTitle("Confirm Registration");
                builderR.setMessage("Are you sure want to confirm?");
                builderR.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        check(Email);
                        check(Name);
                        check(Pass);
                        check(CPass);
                        String password = Pass.getText().toString();
                        if (valid) {
                            if (!password.equals(CPass.getText().toString())) {
                                Pass.setError("Password not match!");
                            } else {
                                final String sName = Name.getText().toString();
                                final String sEmail = Email.getText().toString();
                                final String sPass = Pass.getText().toString();
                                final String sStatus = "Working";
                                final Boolean IsAdmin = true;
                                fAuth.createUserWithEmailAndPassword(sEmail, sPass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(getActivity(), "new staff account created", Toast.LENGTH_SHORT).show();
                                        final FirebaseUser user = fAuth.getCurrentUser();
                                        reference = fBase.getReference("Staff").child(user.getUid());
//                                        Staff  st = new Staff(sEmail,sName,sPass,IsAdmin,sStatus);
                                        Map<String, Object> staffInfo = new HashMap<>();
                                        staffInfo.put("StaffEmail", sEmail);
                                        staffInfo.put("StaffName", sName);
                                        staffInfo.put("StaffPassword", sPass);
                                        staffInfo.put("StaffStatus", sStatus);
                                        staffInfo.put("IsAdmin", IsAdmin);
                                        reference.setValue(staffInfo);
                                        Email.setText("");
                                        Name.setText("");
                                        Pass.setText("");
                                        CPass.setText("");

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Failed to register staff account", Toast.LENGTH_SHORT).show();
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

            case R.id.btnClear:
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
