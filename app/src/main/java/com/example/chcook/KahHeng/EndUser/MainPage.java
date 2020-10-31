package com.example.chcook.KahHeng.EndUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.User;
import com.example.chcook.KahHeng.EndUser.DA.UserDA;
import com.example.chcook.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private Toolbar toolbar2;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FirebaseAuth firebaseAuth;
    private TabLayout tabLayout;
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
        toolbar2 = findViewById(R.id.toolbar2);
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
// set navigation listener
//        toolbar.inflateMenu(R.menu.manage_menu);

        tabLayout = findViewById(R.id.tabLayout);
        settablayout();
    }

    //select your video
    public void settablayout() {

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Video")) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.myNavHostFragment, new VideoManagement());
                    fragmentTransaction.commit();
                } else {
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.myNavHostFragment, new StepManagement());
                    fragmentTransaction.commit();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
                Toast.makeText(MainPage.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        })
        ;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        toolbar2.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        switch (item.getItemId()) {
            case R.id.profile:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.myNavHostFragment, new UploadVideo());
//                fragmentTransaction.replace(R.id.myNavHostFragment, new PlayVideo());
//                fragmentTransaction.replace(R.id.myNavHostFragment, new ShowCookingStep());
//                fragmentTransaction.replace(R.id.myNavHostFragment, new DisplayRecipes());
//                fragmentTransaction.replace(R.id.myNavHostFragment, new PremiumRecipeAdapter());
                fragmentTransaction.replace(R.id.myNavHostFragment, new UserProfile());
                fragmentTransaction.commit();
                progressBar.setVisibility(View.GONE);
                break;
            case R.id.home:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.myNavHostFragment, new Home());
                fragmentTransaction.commit();
                progressBar.setVisibility(View.GONE);
                break;
            case R.id.myVideos:
                toolbar2.setVisibility(View.VISIBLE);
//                toolbar.inflateMenu(R.menu.manage_menu);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                progressBar.setVisibility(View.GONE);
                if (tabLayout.getSelectedTabPosition() == 0) {
                    fragmentTransaction.replace(R.id.myNavHostFragment, new VideoManagement());
                } else {
                    fragmentTransaction.replace(R.id.myNavHostFragment, new StepManagement());
                }
                fragmentTransaction.commit();
                break;
            case R.id.premium:
                String type=user.getType();
                if (user.getType() == null || !type.equals("Premium")) {
                    startActivity(new Intent(getApplicationContext(), Pay.class));
                    Toast.makeText(MainPage.this, "no", Toast.LENGTH_SHORT).show();
                } else {
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.myNavHostFragment, new premium());
                    fragmentTransaction.commit();
                    progressBar.setVisibility(View.GONE);
                }
                break;

            case R.id.history:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.myNavHostFragment, new HistoryUI());
                fragmentTransaction.commit();
                progressBar.setVisibility(View.GONE);
                break;

            case R.id.favorite:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.myNavHostFragment, new AddToFavorite());
                fragmentTransaction.commit();
                progressBar.setVisibility(View.GONE);
                break;


            case R.id.Logout:
                Logout();
                break;
        }
        return false;
    }


}
