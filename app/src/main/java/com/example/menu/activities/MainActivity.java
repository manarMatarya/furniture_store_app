package com.example.menu.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.menu.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fAuth = FirebaseAuth.getInstance();

        Handler handler;
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this, Registeration.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainScreen.class));
            finish();
        }
    }
}