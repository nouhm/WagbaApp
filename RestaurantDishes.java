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
import com.example.wagbaapplication.databinding.ActivityMainBinding;
import com.example.wagbaapplication.databinding.ActivityDishesBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestaurantDishes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityDishesBinding binding;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    DishesAdapter adapter;
    String restaurantName;
    String loginId;
    // int cartSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDishesBinding.inflate(getLayoutInflater());
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
        String restaurantNumberSent = intent.getStringExtra("restaurantNumber");
        loginId = intent.getStringExtra("LoginId");
        int restaurantNumber = Integer.parseInt(restaurantNumberSent);

        // db
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Restaurants");
        DatabaseReference myRef2 = database.getReference("Cart");

        Log.d("dishes", "arrived dishes class from restaurant number " + restaurantNumber);


        // recycler view
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        ArrayList<DishesModel> dishesArrayList= new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int iterator = 0;
                    for(DataSnapshot ds : snapshot.getChildren()){
                        if (iterator == restaurantNumber) {
                            Log.d("dishes", "for restaurant number " + restaurantNumber);
                            Log.d("dishes", "for restaurant name " + restaurantName);
                            restaurantName = ds.getKey();
                            myRef.child(restaurantName).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        for(DataSnapshot ds : snapshot.getChildren()){
                                            String key = ds.getKey();
                                            Log.d("dishes", "for restaurant " + restaurantName);
                                            DishesModel x = new DishesModel();
                                            x.setName(key);
                                            Float value = ds.getValue(Float.class);
                                            x.setPrice(value);
                                            dishesArrayList.add(x);
                                        }

                                    }
                                    adapter = new DishesAdapter(RestaurantDishes.this,
                                            dishesArrayList,
                                            new ClickListener() {
                                                @Override
                                                public void click(int index) {
                                                    // what to do on click ?
                                                    myRef.child(restaurantName).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                int iterator2 = 0;
                                                                for (DataSnapshot ds : snapshot.getChildren()) {
                                                                    if (iterator2 == index) {
                                                                        String dishName = ds.getKey();
                                                                        myRef2.child(loginId).child(dishName).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                if (snapshot.exists()) {
                                                                                    int currentValue = snapshot.getValue(Integer.class);
                                                                                    Log.d("dishes", "dish exists in db with value " + currentValue);
                                                                                    currentValue++;
                                                                                    myRef2.child(loginId).child(dishName).setValue(currentValue);
                                                                                } else {
                                                                                    myRef2.child(loginId).child(dishName).setValue(1);
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                            }
                                                                        });
                                                                        Log.d("dishes", "for user " + loginId);
                                                                        Log.d("dishes", "for dish " + dishName);
                                                                    }
                                                                    iterator2++;
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            });
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(RestaurantDishes.this));
                                    return;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }

                            });
                        }
                        iterator ++ ;
                    }
                }
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
        Intent i = new Intent(RestaurantDishes.this, RestaurantDishes.class);
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_cart: {
                i = new Intent(RestaurantDishes.this, CartActivity.class);
                break;
            }
            case R.id.nav_restaurants: {
                i = new Intent(RestaurantDishes.this, MainActivity.class);
                break;
            }
            case R.id.nav_history: {
                i = new Intent(RestaurantDishes.this, HistoryActivity.class);
                break;
            }
            case R.id.nav_logout: {
                i = new Intent(RestaurantDishes.this, LoginActivity.class);
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