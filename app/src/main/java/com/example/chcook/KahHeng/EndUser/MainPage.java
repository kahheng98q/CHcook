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
import com.example.chcook.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        firebaseAuth = FirebaseAuth.getInstance();
        String uid=firebaseAuth.getCurrentUser().getUid();
        String currentemail=firebaseAuth.getCurrentUser().getEmail();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users").child(uid);
        Map<String, Object> addemail = new HashMap<>();
        addemail.put("email", currentemail);

        ref.updateChildren(addemail);
//        ref.setValue(new User(null,firebaseAuth.getCurrentUser().getEmail(),null,null,null));
//
//        DatabaseReference postsRef = ref.child("Users");
//
//        DatabaseReference newPostRef = postsRef.push();
//        newPostRef.setValue(new User(null,firebaseAuth.getCurrentUser().getEmail(),null,null,null));
//
//// We can also chain the two calls together

//        ref.setValue(new User(null,firebaseAuth.getCurrentUser().getEmail(),null,null,null,null));
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
        View header = navigationView.getHeaderView(0);
        TextView username = header.findViewById(R.id.username);
        TextView email = header.findViewById(R.id.email);
        CircleImageView pic = header.findViewById(R.id.profile);
//        pic.setImageURI();
        Glide.with(this)
                .asBitmap()
                .load(firebaseAuth.getCurrentUser().getPhotoUrl())
                .into(pic);


        username.setText(firebaseAuth.getCurrentUser().getDisplayName());
        email.setText(firebaseAuth.getCurrentUser().getEmail());
//        Log.d("tag", "onCreate " + firebaseAuth.getCurrentUser().getPhotoUrl().toString());
// set navigation listener
//        toolbar.inflateMenu(R.menu.manage_menu);
        navigationView.setNavigationItemSelectedListener(this);
        tabLayout = findViewById(R.id.tabLayout);

        if (toolbar2.getVisibility() == View.VISIBLE) {

        }
//        Toast.makeText(MainPage.this, toolbar2.getVisibility(), Toast.LENGTH_SHORT).show();
        settablayout();

//        Log.d("tag","onCreate "+firebaseAuth.getCurrentUser().getEmail()+firebaseAuth.getCurrentUser().getDisplayName());

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
                fragmentTransaction.replace(R.id.myNavHostFragment, new UploadVideo());
//                fragmentTransaction.replace(R.id.myNavHostFragment, new PlayVideo());
//                fragmentTransaction.replace(R.id.myNavHostFragment, new ShowCookingStep());
//                fragmentTransaction.replace(R.id.myNavHostFragment, new DisplayRecipes());
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
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
//                DatabaseReference ref = database.getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                progressBar.setVisibility(View.GONE);
//                                User user = dataSnapshot.getValue(User.class);
                               String type= dataSnapshot.child("type").getValue(String.class);
                                if (type==null ||!type.equals("Premium")) {
                                    startActivity(new Intent(getApplicationContext(), Pay.class));
                                    Toast.makeText(MainPage.this, "no", Toast.LENGTH_SHORT).show();
                                } else {
                                    fragmentManager = getSupportFragmentManager();
                                    fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.myNavHostFragment, new premium());
                                    fragmentTransaction.commit();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    final DatabaseReference users = database.getReference("Users");
//                    users.orderByChild(firebaseAuth.getCurrentUser().getEmail());


                break;

            case R.id.history:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.myNavHostFragment, new History());
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
