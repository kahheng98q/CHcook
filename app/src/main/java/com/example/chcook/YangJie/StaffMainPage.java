package com.example.chcook.YangJie;

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
import android.widget.Toast;

import com.example.chcook.R;
import com.example.chcook.YangJie.StaffLoginAndManagement.Fragment_addStaff;
import com.example.chcook.YangJie.StaffLoginAndManagement.StaffLogin;
import com.example.chcook.YangJie.StaffLoginAndManagement.StaffPersonalAccount;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class StaffMainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main_page);

        drawerLayout = findViewById(R.id.drawer_staff);
        navigationView = findViewById(R.id.nav_view_staff);
        toolbar = findViewById(R.id.toolbarStaff);
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        //load default fragment
        fragmentManager =getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_staff,new MainFragment_staff());
        fragmentTransaction.commit();


        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_add_staff);
    }

    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        navigationView.setCheckedItem(item.getItemId());
        switch (item.getItemId()) {

            case R.id.nav_home:
                //navigationView.setCheckedItem(R.id.nav_home);
                fragmentManager =getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff,new MainFragment_staff());
                fragmentTransaction.commit();
                break;
            case R.id.nav_add_staff:
//                navigationView.setCheckedItem(R.id.nav_add_staff);
                fragmentManager =getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff,new Fragment_addStaff());
                fragmentTransaction.commit();
                break;
            case R.id.nav_personal_acc:
                Intent intent1 = new Intent(this, StaffPersonalAccount.class);
                startActivity(intent1);
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent =new Intent(getApplicationContext(),StaffLogin.class);
                startActivity(intent);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
