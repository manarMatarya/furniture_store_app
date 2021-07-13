package com.example.menu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.menu.R;
import com.example.menu.fragments.Cart;
import com.example.menu.fragments.Favourite;
import com.example.menu.fragments.about;
import com.example.menu.fragments.main_fragment;
import com.example.menu.fragments.profile;
import com.example.menu.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainScreen extends AppCompatActivity {
    Fragment selectedFragment = null;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        mAuth=FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference(mAuth.getUid()).child("profile");

        Intent intent=getIntent();
        String userName=intent.getStringExtra("name");
        String userEmail=intent.getStringExtra("email");
        String userAddress=intent.getStringExtra("address");
        String userPassword=intent.getStringExtra("password");
        User user = (new User(userName,userEmail,userAddress,userPassword));
        reference.setValue(user);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(listener);
        selectedFragment = new main_fragment();
        loadCalFragment(selectedFragment);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    selectedFragment = new main_fragment();
                    break;
                case R.id.favourite:
                    selectedFragment = new Favourite();
                    break;
                case R.id.cart:
                    selectedFragment = new Cart();
                    break;
                case R.id.navmore:
                    PopupMenu pm = new PopupMenu(getApplicationContext(), findViewById(R.id.navmore));
                    pm.getMenuInflater().inflate(R.menu.popup_menu, pm.getMenu());
                    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.profile:
                                    selectedFragment = new profile();
                                    break;
                                case R.id.call:
                                    dialContactPhone("0598576933");
                                    break;
                                case R.id.about:
                                    selectedFragment = new about();
                                    break;
                                case R.id.logout:
                                    FirebaseAuth.getInstance().signOut();
                                    Toast.makeText(MainScreen.this, "logout succecfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainScreen.this, Registeration.class));
                                    break;
                            }
                            return true;
                        }
                    });
                    pm.show();

            }
            loadCalFragment(selectedFragment);
            return true;
        }
        };

    public void loadCalFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_container, fragment);
        ft.commit();
    }
    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }
}
