package com.example.chcook.KahHeng.EndUser.DA;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chcook.Domain.Recipes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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

public class RecipeDA {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private ArrayList<Recipes> recipes = new ArrayList<>();
    private String recipeKey = "";

    public RecipeDA() {
        connectFirebase();
    }

    public void connectFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public String getRecipeKey() {
        return recipeKey;
    }

    public void setRecipeKey(String recipeKey) {
        this.recipeKey = recipeKey;
    }

    public void createRecipe(Recipes recipe) {
        String uid = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference ref = database.getReference("Users").child(uid).child("Recipe");
        String recipeId = ref.push().getKey();
        DatabaseReference videoref = database.getReference("Recipes").child(recipeId);
        Map<String, Object> addid = new HashMap<>();
        addid.put(recipeId, "true");
//                                    videoid
        Map<String, Object> addURL = new HashMap<>();
        addURL.put("Image", recipe.getImageUrl());
        addURL.put("Title", recipe.getTitle());
        addURL.put("Description", recipe.getDescription());
//        addURL.put("view",0);
        addURL.put("UploadDate", getCurrentTimeStamp());
//                                    addURL.put("id",videoid);
        ref.updateChildren(addid);
        videoref.updateChildren(addURL);
    }

    public interface RecipesCallback {
        ArrayList<Recipes> onCallback(ArrayList<Recipes> recipes);
    }

    public interface RecipeSingleCall {
        Recipes onCallVideo(Recipes recipe);
    }

    public void getUploadedRecipe(final RecipesCallback recipesCallback) {
        DatabaseReference ref = database.getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Recipe");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        setRecipeKey(child.getKey());
                        getAllRecipes(recipesCallback);

                    }

                } else {
                    recipesCallback.onCallback(new ArrayList<Recipes>());
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAllRecipes(final RecipesCallback recipesCallback) {
        DatabaseReference videoRef = database.getReference().child("Recipes").child(recipeKey);
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
                            if (child.getKey().equals("Title")) {
                                name = child.getValue().toString();
                            }
                            if (child.getKey().equals("UploadDate")) {
                                time = Long.valueOf(child.getValue().toString());

                            }
                        }
                        recipes.add(new Recipes(dataSnapshot.getKey(), name, url, getDate(time)));
                        recipesCallback.onCallback(recipes);
                    }

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
        ;
    }

    public void getAllRecipesInPremium(final RecipesCallback recipesCallback){
        DatabaseReference videoref = database.getReference("Recipes");
        videoref.orderByChild("Uploaddate").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    String title= dataSnapshot.child("Title").getValue(String.class);
                    String url= dataSnapshot.child("Image").getValue(String.class);
                    String desc= dataSnapshot.child("Description").getValue(String.class);
                    Long date= dataSnapshot.child("UploadDate").getValue(Long.class);

//                Log.d("test", dataSnapshot.getKey());
                    Long formatedDate = Long.valueOf(date);

                    recipes.add(new Recipes(dataSnapshot.getKey(),title, desc,url, getDate(formatedDate)));
                    recipesCallback.onCallback(recipes);

//                    adapter.notifyDataSetChanged();

                }

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
