package com.example.chcook.YangJie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chcook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    private EditText email;
    private Button btnRecover,btnBack;
    private ProgressBar pg;
    private RelativeLayout pgb;
    private Boolean valid = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        email = findViewById(R.id.txtEmailRecover);
        pgb = findViewById(R.id.progressBRecoverB);
        btnRecover = findViewById(R.id.btnRecover);
        btnBack = findViewById(R.id.btnBackStaffLoginPage);
        pg = findViewById(R.id.progressBRecover);
        final FirebaseAuth fAuth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPassword.this,StaffLogin.class);
                startActivity(intent);
            }
        });

        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                check(email);
                if (valid) {
                    pg.setVisibility(v.VISIBLE);
                    pgb.setVisibility(v.VISIBLE);
                    fAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pg.setVisibility(v.GONE);
                            pgb.setVisibility(v.GONE);
                            if (task.isSuccessful()) {
                                email.setText("");
                                Toast.makeText(ForgetPassword.this, "Password send to your email", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ForgetPassword.this, "Wrong email address", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    private boolean check(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Do not leave empty");
            valid = false;
        } else {
            textField.setError(null);
            valid = true;
        }
        return valid;
    }
}
