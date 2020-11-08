package com.example.chcook.YangJie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chcook.KahHeng.EndUser.GUI.UserAuthentication.Login;
import com.example.chcook.R;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StaffLogin extends AppCompatActivity {
    private Button btnLogin;
    private EditText email, pass;
    private FirebaseAuth fAuth;
    private Boolean valid;
    private FloatingActionButton btnUserLoginPage;
    private ProgressBar pg;
    private TextView forget;
    private RelativeLayout pgb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);
        fAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnLogin);
        btnUserLoginPage = findViewById(R.id.btnUserLoginPage);
        email = findViewById(R.id.staffLoginEmail);
        pass = findViewById(R.id.txtPassword);
        pg = findViewById(R.id.progressBarLogin1);
        pgb = findViewById(R.id.progressBarLogin1B);
        pg.setVisibility(View.INVISIBLE);
        forget = findViewById(R.id.txtForgetPass);

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),ForgetPassword.class);
                startActivity(intent);

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                pg.setVisibility(v.VISIBLE);
                pgb.setVisibility(v.VISIBLE);

                check(email);
                check(pass);
                if (valid) {
                    fAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            DatabaseReference staffDB = FirebaseDatabase.getInstance().getReference("Staff");
                            Query query = staffDB.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        String status = dataSnapshot.child("Status").getValue(String.class);
                                        if (status.equals("Working")) {
                                            if (fAuth.getCurrentUser().isEmailVerified()) {
                                                String pos = dataSnapshot.child("StaffType").getValue(String.class);
                                                pg.setVisibility(v.GONE);
                                                pgb.setVisibility(v.GONE);
                                                Toast.makeText(StaffLogin.this, "Welcome", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), StaffMainPage.class);
                                                intent.putExtra("position", pos);
                                                intent.putExtra("page", "main");
                                                startActivity(intent);
                                            } else {
                                                pg.setVisibility(v.GONE);
                                                pgb.setVisibility(v.GONE);
                                                Toast.makeText(StaffLogin.this, "Please verify your email address", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            pg.setVisibility(v.GONE);
                                            pgb.setVisibility(v.GONE);
                                            Toast.makeText(StaffLogin.this, "You are not allowed to sign in", Toast.LENGTH_SHORT).show();
                                        }


                                    } else {
                                        pg.setVisibility(v.GONE);
                                        pgb.setVisibility(v.GONE);
                                        Toast.makeText(StaffLogin.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pg.setVisibility(v.GONE);
                            pgb.setVisibility(v.GONE);
                            Toast.makeText(StaffLogin.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        btnUserLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StaffLogin.this);
                builder.setTitle("User Login Page");
                builder.setMessage("Are you sure want to back to user login page?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StaffLogin.this);
        builder.setTitle("User Login Page");
        builder.setMessage("Are you sure want to back to user login page?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private Boolean check(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Do not leave empty");
            pg.setVisibility(View.INVISIBLE);
            pgb.setVisibility(View.INVISIBLE);
            valid = false;
        } else {
            textField.setError(null);
            valid = true;
        }
        return valid;
    }
}

