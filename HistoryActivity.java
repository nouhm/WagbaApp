package com.example.wagbaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.wagbaapplication.databinding.ActivityHistoryBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityHistoryBinding binding;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    HistoryAdapter adapter;

    String loginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

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

        // db
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Order");


        // recycler view
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        ArrayList<OrderHistoryModel> orderHistoryArrayList= new ArrayList<>();

        myRef.child(loginId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        final String key = ds.getKey();
                        OrderHistoryModel x = new OrderHistoryModel();
                        x.setOrderNumber(key);
                        String value = ds.getValue(String.class);
                        x.setStatus(value);
                        orderHistoryArrayList.add(x);
                    }
                }
                adapter = new HistoryAdapter(HistoryActivity.this, orderHistoryArrayList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        Intent i = new Intent(HistoryActivity.this, HistoryActivity.class);;
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_cart: {
                i = new Intent(HistoryActivity.this, CartActivity.class);
                break;
            }
            case R.id.nav_restaurants: {
                i = new Intent(HistoryActivity.this, MainActivity.class);
                break;
            }
            case R.id.nav_history: {
                break;
            }
            case R.id.nav_profile: {
                i = new Intent(HistoryActivity.this, ProfileActivity.class);
                break;
            }
            case R.id.nav_logout: {
                i = new Intent(HistoryActivity.this, LoginActivity.class);
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