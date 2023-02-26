package com.example.wagbaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.wagbaapplication.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// List of available restaurants
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , ClickListener{

    ActivityMainBinding binding;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    RestaurantsAdapter adapter;
    String loginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
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

        // db
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Restaurants");


        // recycler view
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        ArrayList<RestaurantsModel> restaurantsArrayList= new ArrayList<>();

        ClickListener l = new ClickListener() {
            @Override
            public void click(int index) {
                Intent i = new Intent(MainActivity.this, RestaurantDishes.class);
                String restaurantNumber = String.valueOf(index);
                i.putExtra("restaurantNumber", restaurantNumber);
                i.putExtra("LoginId", loginId);
                Log.d("restaurants", "restaurant number " + index);
                startActivity(i);
            }
        };

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        String key = ds.getKey();
                        RestaurantsModel x = new RestaurantsModel();
                        x.setName(key);
                        restaurantsArrayList.add(x);
                    }

                }
                adapter = new RestaurantsAdapter(MainActivity.this, restaurantsArrayList, l);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
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
        Intent i = new Intent(MainActivity.this, MainActivity.class);;
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_cart: {
                i = new Intent(MainActivity.this, CartActivity.class);
                break;
            }
            case R.id.nav_restaurants: {
                break;
            }
            case R.id.nav_history: {
                i = new Intent(MainActivity.this, HistoryActivity.class);
                break;
            }
            case R.id.nav_profile: {
                i = new Intent(MainActivity.this, ProfileActivity.class);
                break;
            }
            case R.id.nav_logout: {
                i = new Intent(MainActivity.this, LoginActivity.class);
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