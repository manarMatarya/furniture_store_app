package com.example.menu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.menu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registeration extends AppCompatActivity {
    FirebaseAuth fAuth;
    EditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        email=findViewById(R.id.loginemail);
        password=findViewById(R.id.loginpassword);

        fAuth = FirebaseAuth.getInstance();

        System.setProperty("https.protocols", "TLSv1.1");
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainScreen.class));
            finish();
        }
    }

    public void login(View view) {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if(TextUtils.isEmpty(userEmail)){
            email.setError("email is required");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            email.setError("unvalid email");
            return;
        }
        if(TextUtils.isEmpty(userPassword)){
            password.setError("password is required");
            return;
        }
        if(userPassword.length() < 6){
            password.setError("password must be >=6 characters");
            return;
        }

        fAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), MainScreen.class));
                }else
                    Toast.makeText(Registeration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }

    public void register(View view) {
        startActivity(new Intent(getApplicationContext(), Register_form.class));
    }


}