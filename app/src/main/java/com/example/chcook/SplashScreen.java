package com.example.chcook;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chcook.KahHeng.EndUser.Login;
import com.example.chcook.KahHeng.EndUser.MainPage;

public class SplashScreen  extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}
