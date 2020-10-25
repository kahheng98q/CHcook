package com.example.chcook.KahHeng.EndUser.DA;

import androidx.annotation.NonNull;

import com.example.chcook.Domain.CookingSteps;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CookingStepDA {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private ArrayList<CookingSteps> cookingSteps = new ArrayList<>();
    private String recipeKey = "";
    private String stepKey="";
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

    public interface StepsCallback {
        ArrayList<CookingSteps> onCallback(ArrayList<CookingSteps> recipes);
    }
    public void getUploadedCooking(final StepsCallback stepsCallback) {
        DatabaseReference ref = database.getReference("Recipe").child(recipeKey);
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
        videoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String url = "";
                    String name = "";
                    Long time = 0L;
                    if (dataSnapshot.exists()) {

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.getKey().equals("Image")) {
                                url = child.getValue().toString();
                            }
                            if (child.getKey().equals("Descprition")) {
                                name = child.getValue().toString();
                            }
                        }
                        cookingSteps.add(new CookingSteps(stepKey, name, url));
                        stepsCallback.onCallback(cookingSteps);
                    }

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
        ;
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
