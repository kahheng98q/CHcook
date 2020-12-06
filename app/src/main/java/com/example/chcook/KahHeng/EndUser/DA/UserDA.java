package com.example.chcook.KahHeng.EndUser.DA;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chcook.Domain.User;
import com.example.chcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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

    public String getVideokey() {
        return videokey;
    }

    public void setVideokey(String videokey) {
        this.videokey = videokey;
    }

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

    public void retrieveUserInfo(final UserCallback userCallback) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
//                DatabaseReference ref = database.getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = new User();
                if (dataSnapshot.exists()) {
                    String type = dataSnapshot.child("type").getValue(String.class);
                    String email = dataSnapshot.child("Email").getValue(String.class);
                    String image = dataSnapshot.child("Image").getValue(String.class);
                    String name = dataSnapshot.child("Name").getValue(String.class);
                    String pass = dataSnapshot.child("Password").getValue(String.class);
//                    String status = dataSnapshot.child("Banned").getValue(String.class);

                    if (dataSnapshot.child("Status").child("Status").getValue() != null) {
                        String status = dataSnapshot.child("Status").child("Status").getValue(String.class);
                        user = new User(status, email, image, type, name, pass);
                        userCallback.onCallback(user);

                    } else {
                        user = new User("", email, image, type, name, pass);
                        userCallback.onCallback(user);
                    }
//
//                    user = new User(status, email, image, type, name, pass);
//                    userCallback.onCallback(user);
                } else {
                    user = CreateUserInGoogleLogin();
//                    user = new User("", email, image, type, name, pass);
                    userCallback.onCallback(user);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setUserBasedOnVideo(final UserCallback userCallback) {
        DatabaseReference ref = database.getReference().child("Users");
        ref.orderByChild("video").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    if (child.getKey().equals("video")) {
                        Map<String, Boolean> videoMap = (Map<String, Boolean>) child.getValue();

                        for (String userVideoKey : videoMap.keySet()) {
//                              Log.d("test", "message text:"+key);
                            if (userVideoKey.equals(videokey)) {
                                Log.d("test", "message text:" + dataSnapshot.getKey());
                                setUserInfo(userCallback, dataSnapshot.getKey());
                                break;
                            }
                        }

                    }

                }
//                dataSnapshot.getRef();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUserInfo(final UserCallback userCallback, final String key) {
        DatabaseReference userRef = database.getReference().child("Users").child(key);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = new User();
                    String url = "";
                    String name = "";
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("Image")) {
                            url = child.getValue().toString();
                            user.setImage(url);
//                            Glide.with(getContext())
//                                    .asBitmap()
//                                    .load(child.getValue().toString())
//                                    .into(userPro);

                        }
                        if (child.getKey().equals("Name")) {
                            name = child.getValue().toString();
                            user.setName(name);
//                            txtUser.setText(name);
                        }
                    }
                    userCallback.onCallback(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private User CreateUserInGoogleLogin() {
        User user = new User();
        String uid = firebaseAuth.getCurrentUser().getUid();
        String currentemail = firebaseAuth.getCurrentUser().getEmail();
        String name = firebaseAuth.getCurrentUser().getDisplayName();
        String image = firebaseAuth.getCurrentUser().getPhotoUrl().toString();

        DatabaseReference ref = database.getReference("Users").child(uid);
        Map<String, Object> addemail = new HashMap<>();
        addemail.put("Email", currentemail);
        addemail.put("Name", name);
        addemail.put("Image", image);
        ref.updateChildren(addemail);
        return new User("", currentemail, image, null, name, null);
    }

    public void UpdateUser(User user) {
        String uid = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference ref = database.getReference("Users").child(uid);
        Map<String, Object> addemail = new HashMap<>();
        addemail.put("Name", user.getName());
        ref.updateChildren(addemail);
    }

    public void addPremiumStatus() {
        String uid = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference ref = database.getReference("Users").child(uid);

        Map<String, Object> premiumUpdates = new HashMap<>();
        premiumUpdates.put("type", "Premium");
        ref.updateChildren(premiumUpdates);
    }

    public void UpdateUserImage(String ImageUri) {
        String uid = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference ref = database.getReference("Users").child(uid);
        Map<String, Object> addemail = new HashMap<>();
        addemail.put("Image", ImageUri);
        ref.updateChildren(addemail);
    }

}
