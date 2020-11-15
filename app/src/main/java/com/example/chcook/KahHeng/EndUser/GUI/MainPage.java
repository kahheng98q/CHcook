package com.example.chcook.KahHeng.EndUser.GUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.User;
import com.example.chcook.KahHeng.EndUser.GUI.VideoPlaying.AddToFavorite;
import com.example.chcook.KahHeng.EndUser.DA.UserDA;
import com.example.chcook.KahHeng.EndUser.GUI.PremiumFeatures.DisplayRecipes;
import com.example.chcook.KahHeng.EndUser.GUI.UserAuthentication.Login;
import com.example.chcook.KahHeng.EndUser.GUI.VideoPlaying.HistoryUI;
import com.example.chcook.KahHeng.EndUser.GUI.PremiumFeatures.Pay;
import com.example.chcook.KahHeng.EndUser.GUI.UserAuthentication.UserProfile;
import com.example.chcook.KahHeng.EndUser.GUI.VideoAndRecipeManagement.VideoManagement;
import com.example.chcook.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ProgressBar progressBar;
    private UserDA userDA = new UserDA();
    private User user = new User();
    TextView navtxtuUsername = null;
    TextView navtxtEmail = null;
    CircleImageView navImageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

////set navigation
        progressBar = findViewById(R.id.progressBar);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar1);
//        toolbar2 = findViewById(R.id.toolbar2);
        navigationView = findViewById(R.id.navigationView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


// load default fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.myNavHostFragment, new Home());
        fragmentTransaction.commit();
//display user email and name in top of navigation

//        pic.setImageURI();
        userDA.retrieveUserInfo(new UserDA.UserCallback() {
            @Override
            public User onCallback(User getuser) {
                View header = navigationView.getHeaderView(0);
                navtxtuUsername = header.findViewById(R.id.username);
                navtxtEmail = header.findViewById(R.id.email);
                navImageView = header.findViewById(R.id.profile);

                user = getuser;
                if(user.getStatus().toUpperCase().equals("BANNED")){
//                    if()
                    Toast.makeText(MainPage.this, "Your have been banned.", Toast.LENGTH_SHORT).show();
                    Logout();

                }
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(user.getImage())
                        .into(navImageView);


                navtxtuUsername.setText(user.getName());
                navtxtEmail.setText(user.getEmail());
//                Toast.makeText(MainPage.this, user.getImage(), Toast.LENGTH_SHORT).show();
                return getuser;
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

    }



    //Logout
    public void Logout() {
        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                .signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(getApplicationContext(), Login.class));

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainPage.this, "Fail to Logout", Toast.LENGTH_SHORT).show();
            }
        })
        ;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
//        toolbar2.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        switch (item.getItemId()) {
            case R.id.profile:
                toolbar.setTitle("User Profile");
//                Bundle bundle = new Bundle();
//                bundle.putString("Email", user.getEmail());
//                bundle.putString("Name", user.getName());
//                bundle.putString("ImageUri",user.getImage());
//
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.myNavHostFragment,new UserProfile());
                fragmentTransaction.addToBackStack(null).commit();
                progressBar.setVisibility(View.GONE);
                break;
            case R.id.home:
                toolbar.setTitle("Home");
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.myNavHostFragment, new Home());
                fragmentTransaction.addToBackStack(null).commit();
                progressBar.setVisibility(View.GONE);
                break;
            case R.id.myVideos:
                toolbar.setTitle("Video Management");
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                progressBar.setVisibility(View.GONE);
                fragmentTransaction.replace(R.id.myNavHostFragment, new VideoManagement());
                fragmentTransaction.addToBackStack(null).commit();
                break;
            case R.id.premium:

                String type=user.getType();
                if (user.getType() == null || !type.equals("Premium")) {
                    startActivity(new Intent(getApplicationContext(), Pay.class));
//                    Toast.makeText(MainPage.this, "no", Toast.LENGTH_SHORT).show();
                } else {
                    toolbar.setTitle("Recipe");
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.myNavHostFragment, new DisplayRecipes());
                    fragmentTransaction.addToBackStack(null).commit();
                    progressBar.setVisibility(View.GONE);
                }
                break;

            case R.id.history:
                toolbar.setTitle("History");
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.myNavHostFragment, new HistoryUI());
                fragmentTransaction.addToBackStack(null).commit();
                progressBar.setVisibility(View.GONE);
                break;

            case R.id.favorite:
                toolbar.setTitle("Favorite List");
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.myNavHostFragment, new AddToFavorite());
                fragmentTransaction.addToBackStack(null).commit();
                progressBar.setVisibility(View.GONE);
                break;

            case R.id.Logout:
                Logout();
                break;
        }

        return false;
    }


    @Override
    public void onBackPressed() {
        FragmentManager fm =getSupportFragmentManager();

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }  if (fm.getBackStackEntryCount() > 0) {
//            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        }else {
            AlertDialog.Builder builderR = new AlertDialog.Builder(this);
            builderR.setTitle("Quit");
            builderR.setMessage("Are you sure want to quit?");
            builderR.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Logout();
                    Toast.makeText(getApplicationContext(),"Logout success",Toast.LENGTH_SHORT).show();
                }
            });
            builderR.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialogR = builderR.create();
            dialogR.show();
        }

    }


}
