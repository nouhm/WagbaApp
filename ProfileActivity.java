package com.example.wagbaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.wagbaapplication.databinding.ActivityLoginBinding;
import com.example.wagbaapplication.databinding.ActivityProfileBinding;
import com.google.android.material.navigation.NavigationView;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ActivityProfileBinding binding;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    String id;
    String loginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // nav menu
        drawerLayout = binding.myDrawerLayout;
        navigationView = binding.navView;

        navigationView.bringToFront();
        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        loginId = intent.getStringExtra("LoginId");

        // room db, get id
        ProfileRoomDatabase profileRoomDatabase = ProfileRoomDatabase.getDatabase(getApplicationContext());
        ProfileDao profileDao = profileRoomDatabase.profileDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                id = profileDao.getMid();
                binding.TextViewHelloID.setText("Welcome " + id);

                if (profileDao.getMname() != null) {
                    binding.name.setText(profileDao.getMname());
                }
                if (profileDao.getMphonenb() != null) {
                    binding.phoneNb.setText(profileDao.getMphonenb());
                }
            }
        }).start();

        binding.saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(binding.name.getText());
                String phonenb = String.valueOf(binding.phoneNb.getText());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Profile p = new Profile(id, name, phonenb);
                        profileDao.insert(p);
                    }
                }).start();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent i = new Intent(ProfileActivity.this, ProfileActivity.class);;
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_cart: {
                i = new Intent(ProfileActivity.this, CartActivity.class);
                break;
            }
            case R.id.nav_restaurants: {
                i = new Intent(ProfileActivity.this, MainActivity.class);
                break;
            }
            case R.id.nav_history: {
                i = new Intent(ProfileActivity.this, HistoryActivity.class);
                break;
            }
            case R.id.nav_profile: {
                break;
            }
            case R.id.nav_logout: {
                i = new Intent(ProfileActivity.this, LoginActivity.class);
                ProfileRoomDatabase profileRoomDatabase = ProfileRoomDatabase.getDatabase(getApplicationContext());
                ProfileDao profileDao = profileRoomDatabase.profileDao();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        profileDao.deleteAll();
                    }
                }).start();
                break;
            }
        }
        i.putExtra("LoginId", loginId);
        startActivity(i);
        finish();

        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}