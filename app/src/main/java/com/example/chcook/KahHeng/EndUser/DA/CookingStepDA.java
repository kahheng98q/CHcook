package com.example.chcook.KahHeng.EndUser.DA;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chcook.Domain.CookingSteps;
import com.example.chcook.Domain.Recipes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CookingStepDA {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private ArrayList<CookingSteps> cookingSteps = new ArrayList<>();
    private String recipeKey = "";
    private String stepKey = "";

    public String getRecipeKey() {
        return recipeKey;
    }

    public void setRecipeKey(String recipeKey) {
        this.recipeKey = recipeKey;
    }

    public String getStepKey() {
        return stepKey;
    }

    public void setStepKey(String stepKey) {
        this.stepKey = stepKey;
    }

    public CookingStepDA() {
        connectFirebase();
    }

    public void connectFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public void createStep(CookingSteps cookingStep) {
//        String uid = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference ref = database.getReference("Recipes").child(cookingStep.getRecipes().getRecipeId()).child("CookingSteps");
        String stepId = ref.push().getKey();
        DatabaseReference videoref = database.getReference("CookingSteps").child(stepId);
        Map<String, Object> addid = new HashMap<>();
        addid.put(stepId, "true");
//                                    videoid
        Map<String, Object> addURL = new HashMap<>();
        addURL.put("Image", cookingStep.getImageUrl());
        addURL.put("Description", cookingStep.getDescription());
//        addURL.put("view",0);
//        addURL.put("UploadDate", getCurrentTimeStamp());
//                                    addURL.put("id",videoid);
        ref.updateChildren(addid);
        videoref.updateChildren(addURL);
    }

    public interface StepsCallback {
        ArrayList<CookingSteps> onCallback(ArrayList<CookingSteps> cookingSteps);
    }

    public void getUploadedCooking(final StepsCallback stepsCallback) {
        DatabaseReference ref = database.getReference("Recipes").child(recipeKey).child("CookingSteps");
//        Log.d("test", recipeKey);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        setStepKey(child.getKey());
                        getAllRecipes(stepsCallback);
                    }

                } else {
                    stepsCallback.onCallback(new ArrayList<CookingSteps>());
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAllRecipes(final StepsCallback stepsCallback) {
        DatabaseReference videoRef = database.getReference().child("CookingSteps").child(stepKey);
//        Log.d("test", stepKey);
        videoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String url = "";
                    String desc = "";

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("Image")) {
                            url = child.getValue().toString();
                        }
                        if (child.getKey().equals("Description")) {
                            desc = child.getValue().toString();
                        }
                    }
                    cookingSteps.add(new CookingSteps(stepKey, url, desc,new Recipes(recipeKey)));
                    stepsCallback.onCallback(cookingSteps);

                }else {
                    stepsCallback.onCallback(new ArrayList<CookingSteps>());
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
        ;
    }
    public boolean deleteStep() {
        if (!stepKey.isEmpty() || !stepKey.equals("")&&!recipeKey.isEmpty()) {
            DatabaseReference ref = database.getReference("Recipes").child(recipeKey).child("CookingSteps").child(stepKey);
            ref.removeValue();
        } else {
            Log.d("test", "message text:null");
        }
        return  false;
    }
    public void editCookingStep(String desc) {
        if (!stepKey.isEmpty() || !stepKey.equals("")) {
            DatabaseReference stepRef = database.getReference().child("CookingSteps").child(stepKey);
            Map<String, Object> stepUpdates = new HashMap<>();
            stepUpdates.put("Description", desc);
            stepRef.updateChildren(stepUpdates);
        }
    }
    private String getDate(Long timeStamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timeStamp * 1000);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String date = df.format("dd-MM-yyyy hh:mm", cal).toString();
        return date;
    }

    private long getCurrentTimeStamp() {
        long timestamp = System.currentTimeMillis() / 1000;
        return timestamp;
    }
}
