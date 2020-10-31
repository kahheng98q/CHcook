package com.example.chcook.KahHeng.EndUser.DA;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.chcook.Domain.User;
import com.example.chcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserDA {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private String videokey = "";

    private void connectFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public UserDA() {
        connectFirebase();
    }

    public interface UserCallback {
        User onCallback(User user);
    }

    public void retrieveUserInfo(final UserCallback userCallback){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
//                DatabaseReference ref = database.getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=new User();
                if (dataSnapshot.exists()) {
                    String type= dataSnapshot.child("type").getValue(String.class);
                    String email= dataSnapshot.child("Email").getValue(String.class);
                    String image= dataSnapshot.child("Image").getValue(String.class);
                    String name= dataSnapshot.child("Name").getValue(String.class);
                    String pass=dataSnapshot.child("Password").getValue(String.class);
                    String status=dataSnapshot.child("Banned").getValue(String.class);

                    user=new User(status,email,image,type,name,pass);
                    userCallback.onCallback(user);
                } else {
                    CreateUserInGoogleLogin();
                    userCallback.onCallback(user);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private User CreateUserInGoogleLogin(){
        User user=new User();
        String uid=firebaseAuth.getCurrentUser().getUid();
        String currentemail=firebaseAuth.getCurrentUser().getEmail();
        String name= firebaseAuth.getCurrentUser().getDisplayName();
        String image= firebaseAuth.getCurrentUser().getPhotoUrl().toString();

        DatabaseReference ref = database.getReference("Users").child(uid);
        Map<String, Object> addemail = new HashMap<>();
        addemail.put("Email", currentemail);
        addemail.put("Name", name);
        addemail.put("Image",image);
        ref.updateChildren(addemail);
        return new User(null,currentemail,image,null,name,null);
    }
    public void UpdateUser(User user){
        User tmpuser=new User();
        String uid=firebaseAuth.getCurrentUser().getUid();
        DatabaseReference ref = database.getReference("Users").child(uid);
        Map<String, Object> addemail = new HashMap<>();

        addemail.put("Name", user.getName());
        addemail.put("Image",user.getImage());
        ref.updateChildren(addemail);
    }

}
