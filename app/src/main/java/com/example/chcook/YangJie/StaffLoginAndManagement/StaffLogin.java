package com.example.chcook.YangJie.StaffLoginAndManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chcook.Login;
import com.example.chcook.R;

import com.example.chcook.YangJie.StaffMainPage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class StaffLogin extends AppCompatActivity {
    private Button btnLogin;
    private EditText email, pass;
    private FirebaseAuth fAuth;
    private Boolean valid;
    private FloatingActionButton btnUserLoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);
        fAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnLogin);
        btnUserLoginPage = findViewById(R.id.btnUserLoginPage);
        email = findViewById(R.id.txtEmail);
        pass = findViewById(R.id.txtPassword);
//        FirebaseUser user = fAuth.getCurrentUser();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check(email);
                check(pass);
                if (valid) {
                    fAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(StaffLogin.this,"Welcome",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), StaffMainPage.class);
                            startActivity(intent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(StaffLogin.this,"wrong email or password",Toast.LENGTH_SHORT).show();
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
                builder.setMessage("Are you sure want to back to user login opage?");
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

    private Boolean check(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Do not leave empty");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }
}

