package com.example.chcook.YangJie;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.widget.Button;

import com.example.chcook.R;
import com.karumi.dexter.Dexter;

public class createReport extends AppCompatActivity {
    private Button c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);
        c = findViewById(R.id.btnCreatePdf);

    }
}
