package com.example.chcook.YangJie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chcook.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowBanUser extends AppCompatActivity {

    private CircleImageView userImg;
    private TextView username,email,status,reason,redirect,date,tt;
    private Button btnback,btnBan;
    private String videoId,userId,reportId,IsAdmin,Vtype,VReason,Vdate;
    private Boolean admin;
    private int tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ban_user);
        userImg = findViewById(R.id.BanUserImg);
        username = findViewById(R.id.BanUserName);
        email = findViewById(R.id.BanUserEmail);
        status = findViewById(R.id.BanUserStatus);
        reason = findViewById(R.id.txtBanReason);
        redirect = findViewById(R.id.txtRedirect);
        date = findViewById(R.id.txtBanDate);
        btnback = findViewById(R.id.btnBack);
        tt = findViewById(R.id.txtBanUserType);
        btnBan = findViewById(R.id.btnBan);

        final Bundle extras = getIntent().getExtras();
        if(extras!=null){
            videoId = extras.getString("videoId");
            userId = extras.getString("userId");
            reportId = extras.getString("reportId");
            IsAdmin = extras.getString("position");
            admin = Boolean.parseBoolean(IsAdmin);
//            Toast.makeText(this, IsAdmin, Toast.LENGTH_SHORT).show();
        }
        DatabaseReference databaseReferenceU = FirebaseDatabase.getInstance().getReference("Users");
        final DatabaseReference databaseReferenceR = FirebaseDatabase.getInstance().getReference("Report");
        databaseReferenceR.child(extras.getString("reportId")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reason.setText("Reason : "+dataSnapshot.child("Description").getValue(String.class));
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String latestDate = df.format(dataSnapshot.child("Date").getValue(Long.class));
                String type = dataSnapshot.child("Type").getValue(String.class);
                date.setText("Report Date : "+latestDate);
                Vdate = latestDate;
                VReason = dataSnapshot.child("Description").getValue(String.class);
                Vtype = dataSnapshot.child("Type").getValue(String.class);
                tt.setText("Type : "+type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReferenceU.child(extras.getString("userId")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ban = "";
                if(dataSnapshot.hasChild("Banned")){
                   ban =  dataSnapshot.child("Banned").getValue(String.class);
                }else {
                    ban = "no";
                }
                if(ban.equals("yes")){
                    String stattus = "Banned";
                    status.setText("Status : "+stattus);
                    btnBan.setTag(1);
                    btnBan.setText("unban");
//                    status.setTextColor(Color.parseColor("#FF0000"));
                }else{
                    String stattus = "Approval";
                    btnBan.setTag(2);
                    btnBan.setText("ban");
                    status.setText("Status : "+stattus);
                }
                tag = (Integer) btnBan.getTag();

//                status.setText("User Status :"+dataSnapshot.child("Banned").getValue(String.class));
                email.setText("Email : "+dataSnapshot.child("Email").getValue(String.class));
                username.setText("UserName : "+dataSnapshot.child("Name").getValue(String.class));
                String img = dataSnapshot.child("Image").getValue(String.class);
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(Uri.parse(img))
                        .error(R.drawable.load_error_24dp)
                        .into(userImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderR = new AlertDialog.Builder(ShowBanUser.this);
                if (tag == 1) {
                    builderR.setTitle("Unban this user");
                    builderR.setMessage("Are you sure want to unban?");
                } else {
                    builderR.setTitle("Ban this user");
                    builderR.setMessage("Are you sure want to ban?");
                }

                builderR.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Query query = FirebaseDatabase.getInstance().getReference("Users").child(extras.getString("userId"));
                        HashMap hashMap = new HashMap();
                        if (tag == 1) {
                            hashMap.put("Banned", "no");
                        } else {
                            hashMap.put("Banned", "yes");
                        }

                        query.getRef().updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                admin = Boolean.parseBoolean(IsAdmin);
                                Toast.makeText(ShowBanUser.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                        });
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
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderR = new AlertDialog.Builder(ShowBanUser.this);

                builderR.setTitle("Back");
                builderR.setMessage("Are you sure want to back?");
                builderR.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), StaffMainPage.class);
                        intent.putExtra("page", "banUser");
                        intent.putExtra("position",admin);
                        startActivity(intent);
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
        });
        redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderR = new AlertDialog.Builder(ShowBanUser.this);

                builderR.setTitle("Redirect to video page");
                builderR.setMessage("Are you sure want to show video?");
                builderR.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), ShowBanVideo.class);
                        intent.putExtra("videoId",videoId);
                        intent.putExtra("position",IsAdmin);
                        intent.putExtra("description",VReason);
                        intent.putExtra("type",Vtype);
                        intent.putExtra("page","banUser");
                        intent.putExtra("date",Vdate);
                        startActivity(intent);
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
        });
    }

}
