package com.example.chcook;

import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

        drawerLayout= findViewById(R.id.drawer);
        toolbar= findViewById(R.id.toolbar);
        navigationView= findViewById(R.id.navigationView);
       setSupportActionBar(toolbar);
       getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
       View header=navigationView.getHeaderView(0);
       TextView username=header.findViewById(R.id.username);
       username.setText(firebaseAuth.getCurrentUser().getDisplayName());
        TextView email=header.findViewById(R.id.email);
        email.setText(firebaseAuth.getCurrentUser().getEmail());
        navigationView.setNavigationItemSelectedListener(this);




//        Log.d("tag","onCreate "+firebaseAuth.getCurrentUser().getEmail()+firebaseAuth.getCurrentUser().getDisplayName());

    }

    public void Logout() {
        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(this,new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                .signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(getApplicationContext(),Login.class));

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainPage.this,"Fail",Toast.LENGTH_SHORT).show();
            }
        })
        ;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.profile:
                Toast.makeText(MainPage.this,"Profile",Toast.LENGTH_SHORT).show();
                break;
            case R.id.Logout:
                Logout();
                break;
        }
        return false;
    }
}
