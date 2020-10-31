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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chcook.R;
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
    Boolean position;

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
        View header = ((NavigationView)findViewById(R.id.nav_view_staff)).getHeaderView(0);
        usernameS = header.findViewById(R.id.usernames_staff);
        emailS = header.findViewById(R.id.emails_staff);
        prfStaff = header.findViewById(R.id.profileImage_staff);
        prfStaff = header.findViewById(R.id.profileImage_staff);
        Bundle extras = getIntent().getExtras();
        String page="";
        if(extras!=null){
            page=extras.getString("page");
            position = extras.getBoolean("position");
        }
        String isAdmin = position.toString();

        if(isAdmin.equals("true")){
            isAdmin = "Admin";
        }else{
            isAdmin = "Staff";
        }
        FirebaseUser staff = FirebaseAuth.getInstance().getCurrentUser();
        usernameS.setText(staff.getDisplayName()+" - "+isAdmin);
        emailS.setText(staff.getEmail());
        Glide.with(this)
                .load(staff.getPhotoUrl().toString())
                .into(prfStaff);


        if(page.equals("banVideo")){
            toolbar.setTitle("Ban Video");
            fragmentManager =getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container_staff,new Fragment_banvideo());
            fragmentTransaction.commit();
        }else if (page.equals("banUser")){
            toolbar.setTitle("Ban User");
            fragmentManager =getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container_staff,new Fragment_banUser());
            fragmentTransaction.commit();
        }else{
            toolbar.setTitle("Home");
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
//            super.onBackPressed();
            AlertDialog.Builder builderR = new AlertDialog.Builder(this);
            builderR.setTitle("Quit");
            builderR.setMessage("Are you sure want to quit?");
            builderR.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent =new Intent(getApplicationContext(),StaffLogin.class);
                    startActivity(intent);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Bundle extras = getIntent().getExtras();
        Boolean admin = extras.getBoolean("position");
        String isAdmin = admin.toString();
        switch (item.getItemId()) {

            case R.id.nav_home:
                toolbar.setTitle("Home");
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff, new MainFragment_staff());
                fragmentTransaction.commit();
                break;
            case R.id.nav_add_staff:
                toolbar.setTitle("Add Staff");
                if (isAdmin.equals("true")) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_staff, new Fragment_addStaff());
                    fragmentTransaction.commit();
                } else {
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_staff, new FragmentBlank());
                    fragmentTransaction.commit();
                }
                break;
            case R.id.nav_update_staff:
                toolbar.setTitle("Update Staff");
                if (isAdmin.equals("true")) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_staff, new Fragment_UpdateStaff());
                    fragmentTransaction.commit();
                } else {
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_staff, new FragmentBlank());
                    fragmentTransaction.commit();
                }
                break;

            case R.id.nav_delete_staff:
                toolbar.setTitle("Delete Staff");
                if (isAdmin.equals("true")) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_staff, new Fragment_deleteStaff());
                    fragmentTransaction.commit();
                } else {
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_staff, new FragmentBlank());
                    fragmentTransaction.commit();
                }
                break;
            case R.id.nav_ban_user:
                toolbar.setTitle("Ban User");
                fragmentManager =getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff,new Fragment_banUser());
                fragmentTransaction.commit();
                break;
            case R.id.nav_ban_video:
                toolbar.setTitle("Ban Video");
                fragmentManager =getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff,new Fragment_banvideo());
                fragmentTransaction.commit();
                break;
            case R.id.nav_display_income:
                toolbar.setTitle("Income Status");
                fragmentManager =getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff,new Fragment_ShowIncome());
                fragmentTransaction.commit();
                break;
            case R.id.nav_upd_price:
                toolbar.setTitle("Update Price");
                fragmentManager =getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff,new Fragment_updatePrice());
                fragmentTransaction.commit();
                break;
            case R.id.nav_personal_acc:
                toolbar.setTitle("Manage Profile");
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff,new Fragment_staffownprofile());
                fragmentTransaction.commit();
                break;
            case R.id.nav_report_rate:
                toolbar.setTitle("Video Rating Report");
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff,new Fragment_VideoRatingReport());
                fragmentTransaction.commit();
                break;
            case R.id.nav_logout:
                AlertDialog.Builder builderR = new AlertDialog.Builder(this);
                builderR.setTitle("Logout");
                builderR.setMessage("Are you sure want to logout?");
                builderR.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent =new Intent(getApplicationContext(),StaffLogin.class);
                        startActivity(intent);
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
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
