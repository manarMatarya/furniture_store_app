package com.example.menu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.menu.R;
import com.example.menu.fragments.main_fragment;
import com.example.menu.models.Dish;
import com.example.menu.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register_form extends AppCompatActivity {

    EditText regname,regemail,regaddress,regpassword;
    Button regbtn;
    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    private static final String TAG = "signUp";
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refister_form);

        regname = findViewById(R.id.regname);
        regemail = findViewById(R.id.regemail);
        regaddress = findViewById(R.id.regaddress);
        regpassword = findViewById(R.id.regpassword);
        regbtn = findViewById(R.id.regbtn);
        // Initialize Firebase Auth
        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), MainScreen.class));
            finish();
        }
    }

    public void register(View view) {
        final String userEmail = regemail.getText().toString().trim();
        final String userPassword = regpassword.getText().toString().trim();
        final String userName = regname.getText().toString().trim();
        final String userAddress = regaddress.getText().toString().trim();

        if(TextUtils.isEmpty(userEmail)){
            regemail.setError("email is required");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            regemail.setError("unvalid email");
            return;
        }
        if(TextUtils.isEmpty(userPassword)){
            regpassword.setError("password is required");
            return;
        }
        if(userPassword.length() < 6){
            regpassword.setError("password must be >=6 characters");
            return;
        }

        mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Register_form.this, "User Created", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Register_form.this,MainScreen.class);
                    intent.putExtra("name",userName);
                    intent.putExtra("email",userEmail);
                    intent.putExtra("address",userAddress);
                    intent.putExtra("password",userPassword);
                    startActivity(intent);
                }else
                    Toast.makeText(Register_form.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void login(View view) {
        startActivity(new Intent(getApplicationContext(), Registeration.class));
    }
}