package com.example.chcook.YangJie.StaffLoginAndManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.chcook.R;

public class StaffLogin extends AppCompatActivity {
    private Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);
        btnRegister = findViewById(R.id.btnRegisterStaff);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),StaffRegistration.class);
                startActivity(intent);
            }
        });
    }
}
