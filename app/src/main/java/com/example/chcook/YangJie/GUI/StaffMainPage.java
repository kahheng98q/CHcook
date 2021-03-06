package com.example.chcook.YangJie.GUI;

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
import com.example.chcook.YangJie.GUI.Banning.Fragment_banUser;
import com.example.chcook.YangJie.GUI.Banning.Fragment_banvideo;
import com.example.chcook.YangJie.GUI.Payment.Fragment_ShowIncome;
import com.example.chcook.YangJie.GUI.Payment.Fragment_updatePrice;
import com.example.chcook.YangJie.GUI.ReportGenerate.Fragment_Banning_Report;
import com.example.chcook.YangJie.GUI.ReportGenerate.Fragment_IncomeReport;
import com.example.chcook.YangJie.GUI.ReportGenerate.Fragment_VideoUploadReport;
import com.example.chcook.YangJie.GUI.StaffLoginAndManagement.Fragment_UpdateStaff;
import com.example.chcook.YangJie.GUI.StaffLoginAndManagement.Fragment_addStaff;
import com.example.chcook.YangJie.GUI.StaffLoginAndManagement.Fragment_deleteStaff;
import com.example.chcook.YangJie.GUI.StaffLoginAndManagement.Fragment_staffownprofile;
import com.example.chcook.YangJie.GUI.StaffLoginAndManagement.StaffLogin;
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
    String position;

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
            position = extras.getString("position");
        }



        FirebaseUser staff = FirebaseAuth.getInstance().getCurrentUser();
        usernameS.setText(staff.getDisplayName()+" - "+position);
        emailS.setText(staff.getEmail());
        Glide.with(this)
                .load(staff.getPhotoUrl().toString())
                .into(prfStaff);


        if(page.equals("banVideo")){

            Fragment_banvideo fragment = new Fragment_banvideo();
            Bundle post = new Bundle();
            post.putString("position",position);
            fragment.setArguments(post);
            toolbar.setTitle("Ban Video");
            fragmentManager =getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container_staff,fragment);
            fragmentTransaction.commit();
        }else if (page.equals("banUser")){

            Fragment_banUser ffragment = new Fragment_banUser();
            Bundle posi = new Bundle();
            posi.putString("position",position);
            ffragment.setArguments(posi);
            toolbar.setTitle("Ban User");
            fragmentManager =getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container_staff,ffragment);
            fragmentTransaction.commit();
        }else if(page.equals("income")){

            Fragment_ShowIncome income = new Fragment_ShowIncome();
            Bundle positi = new Bundle();
            positi.putString("position",position);
            income.setArguments(positi);
            toolbar.setTitle("Income Status");
            fragmentManager =getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_staff,income);
            fragmentTransaction.commit();

        } else{
            toolbar.setTitle("Home");
            MainFragment_staff mainfragment = new MainFragment_staff();
            Bundle posit = new Bundle();
            posit.putString("position",position);
            mainfragment.setArguments(posit);
            fragmentManager =getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container_staff,mainfragment);
            fragmentTransaction.commit();
        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    //close drawer and logout
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builderR = new AlertDialog.Builder(this);
            builderR.setTitle("Quit");
            builderR.setMessage("Are you sure want to quit?");
            builderR.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent =new Intent(getApplicationContext(), StaffLogin.class);
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
        String getPosition = extras.getString("position");
        switch (item.getItemId()) {
            case R.id.nav_home:
                toolbar.setTitle("Home");
                MainFragment_staff mainfragment = new MainFragment_staff();
                Bundle posit = new Bundle();
                posit.putString("position",getPosition);
                mainfragment.setArguments(posit);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff, mainfragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_add_staff:
                toolbar.setTitle("Add Staff");
                if (getPosition.equals("Admin")) {
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
                toolbar.setTitle("Update Normal Staff");
                if (getPosition.equals("Admin")) {
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
                toolbar.setTitle("Delete Normal Staff");
                if (getPosition.equals("Admin")) {
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
                Fragment_banUser ffragment = new Fragment_banUser();
                Bundle posi = new Bundle();
                posi.putString("position",getPosition);
                ffragment.setArguments(posi);
                fragmentManager =getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff,ffragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_ban_video:
                toolbar.setTitle("Ban Video");
                Fragment_banvideo fragment = new Fragment_banvideo();
                Bundle post = new Bundle();
                post.putString("position",getPosition);
                fragment.setArguments(post);
                fragmentManager =getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff,fragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_display_income:
                toolbar.setTitle("Income Status");
                Fragment_ShowIncome income = new Fragment_ShowIncome();
                Bundle positi = new Bundle();
                positi.putString("position",getPosition);
                income.setArguments(positi);
                fragmentManager =getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff,income);
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
            case R.id.nav_report_income:
                toolbar.setTitle("Income Report");
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff,new Fragment_IncomeReport());
                fragmentTransaction.commit();
                break;
            case R.id.nav_report_video:
                toolbar.setTitle("Video Upload Report");
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff,new Fragment_VideoUploadReport());
                fragmentTransaction.commit();
                break;
            case R.id.nav_report_type:
                toolbar.setTitle("Banning Report");
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_staff,new Fragment_Banning_Report());
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
