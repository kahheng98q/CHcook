package com.example.chcook.YangJie.GUI.Banning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chcook.R;
import com.example.chcook.YangJie.GUI.StaffMainPage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowBanUser extends AppCompatActivity {

    private CircleImageView userImg;
    private TextView username,email,status,reason,redirect,date,tt;
    private Button btnback,btnBan;
    private String videoId,userId,reportId,position,Vtype,VReason,Vdate,Vposition;
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
        btnback = findViewById(R.id.btnShowIncomeBack);
        tt = findViewById(R.id.txtBanUserType);
        btnBan = findViewById(R.id.btnBan);

        final Bundle extras = getIntent().getExtras();
        if(extras!=null){
            videoId = extras.getString("videoId");
            userId = extras.getString("userId");
            reportId = extras.getString("reportId");
            position = extras.getString("position");

        }
        DatabaseReference databaseReferenceU = FirebaseDatabase.getInstance().getReference("Users");
        final DatabaseReference databaseReferenceR = FirebaseDatabase.getInstance().getReference("Report");
        databaseReferenceR.child(extras.getString("reportId")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String rreason="";
                if(dataSnapshot.hasChild("Description")){
                    rreason = dataSnapshot.child("Description").getValue(String.class);
                    if(rreason.equals("")){
                        rreason="";
                    }
                }

                reason.setText("Reason : "+rreason);
                String type = dataSnapshot.child("Type").getValue(String.class);
                date.setText("Report Date : "+getDate(dataSnapshot.child("Date").getValue(Long.class)));
                Vdate = getDate(dataSnapshot.child("Date").getValue(Long.class));
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
                if(dataSnapshot.hasChild("Status")){
                   ban =  dataSnapshot.child("Status").child("Status").getValue(String.class);
                }else {
                    ban = "Approval";
                }
                if(ban.equals("Banned")){
                    String stattus = "Banned";
                    status.setText("Status : "+stattus);
                    btnBan.setTag(1);
                    btnBan.setText("unban");
                }else{
                    String stattus = "Approval";
                    btnBan.setTag(2);
                    btnBan.setText("ban");
                    status.setText("Status : "+stattus);
                }
                tag = (Integer) btnBan.getTag();
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
                final EditText input = new EditText(ShowBanUser.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                if (tag == 1) {
                    builderR.setTitle("Unban this user");
                    builderR.setMessage("Are you sure want to unban?");
                } else {
                    builderR.setTitle("Ban this user");
                    builderR.setMessage("Please state the reason ban");
                    builderR.setView(input);
                }

                builderR.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Query query = FirebaseDatabase.getInstance().getReference("Users").child(extras.getString("userId")).child("Status");
//                        HashMap hashMap = new HashMap();
//                        if (tag == 1) {
//                            hashMap.put("Status", "Approval");
//                        } else {
//                            hashMap.put("Status", "Banned");
//                        }
                        Map<String, Object> hashMap = new HashMap<>();
                        if (tag == 1) {
                            hashMap.put("Status", "Approval");
                            hashMap.put("Reason","");

                        } else {
                            hashMap.put("Status", "Banned");
                            hashMap.put("Reason", input.getText().toString());
                            hashMap.put("Date", ServerValue.TIMESTAMP);
                        }

                        query.getRef().updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Vposition = position;
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
                        intent.putExtra("position",position);
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
                        intent.putExtra("position",position);
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
    private String getDate(Long timeStamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timeStamp * 1000);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String date = df.format("dd-MM-yyyy", cal).toString();
        return date;
    }

}
