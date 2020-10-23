package com.example.chcook.YangJie;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chcook.R;
import com.example.chcook.YangJie.StaffLoginAndManagement.Fragment_addStaff;
import com.example.chcook.YangJie.StaffLoginAndManagement.Fragment_deleteStaff;
import com.example.chcook.YangJie.StaffLoginAndManagement.Fragment_staffownprofile;
import com.example.chcook.YangJie.StaffLoginAndManagement.StaffLogin;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class StaffMainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView usernameS,emailS;
    CircleImageView prfStaff;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String username,email,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main_page);


//        TextView usernameS = findViewById(R.id.usernames_staff);
//        TextView emailS=(TextView)findViewById(R.id.emails_staff);
        drawerLayout = findViewById(R.id.drawer_staff);
        navigationView = findViewById(R.id.nav_view_staff);
        toolbar = findViewById(R.id.toolbarStaff);
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        View header = ((NavigationView)findViewById(R.id.nav_view_staff)).getHeaderView(0);
        usernameS = header.findViewById(R.id.usernames_staff);
        emailS = header.findViewById(R.id.emails_staff);
        prfStaff = header.findViewById(R.id.profileImage_staff);
        prfStaff = header.findViewById(R.id.profileImage_staff);
        FirebaseUser staff = FirebaseAuth.getInstance().getCurrentUser();
        usernameS.setText(staff.getDisplayName()+" - Staff");
        emailS.setText(staff.getEmail());
        Glide.with(this)
                .load(staff.getPhotoUrl().toString())
                .into(prfStaff);

        Bundle extras = getIntent().getExtras();
        String page="";
        if(extras!=null){
             page=extras.getString("page");
        }
        if(page.equals("banVideo")){
            fragmentManager =getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container_staff,new Fragment_banvideo());
            fragmentTransaction.commit();
        }else{
            fragmentManager =getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container_staff,new MainFragment_staff());
            fragmentTransaction.commit();
        }


        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setCheckedItem(R.id.nav_add_staff);
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
            case R.id.nav_delete_staff:
                fragmentManager =getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff,new Fragment_deleteStaff());
                fragmentTransaction.commit();
                break;
            case R.id.nav_ban_video:
                fragmentManager =getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff,new Fragment_banvideo());
                fragmentTransaction.commit();
                break;
            case R.id.nav_upd_price:
                fragmentManager =getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff,new Fragment_updatePrice());
                fragmentTransaction.commit();
                break;
            case R.id.nav_personal_acc:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff,new Fragment_staffownprofile());
                fragmentTransaction.commit();
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
