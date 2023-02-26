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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.wagbaapplication.databinding.ActivityCartBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

public class CartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityCartBinding binding;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    CartAdapter adapter;
    String loginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
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
        DatabaseReference myRef = database.getReference("Cart");

        // recycler view
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        ArrayList<CartModel> cartArrayList= new ArrayList<>();


        myRef.child(loginId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        final String dishName = ds.getKey();
                        CartModel x = new CartModel();
                        x.setName(dishName);
                        int quantity = ds.getValue(Integer.class);
                        x.setQuantity(quantity);
                        cartArrayList.add(x);

                    }

                }
                adapter = new CartAdapter(CartActivity.this, cartArrayList, new ClickListener() {
                    @Override
                    public void click(int index) {
                        // remove item from cart (from array and from db)
                        myRef.child(loginId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                    int iterator = 0;
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        if (iterator == index) {
                                            String dishName = ds.getKey();
                                            myRef.child(loginId).child(dishName).removeValue();
                                        }
                                        iterator++;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        cartArrayList.remove(index);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                        // , LinearLayoutManager.VERTICAL, false
                    }
                });
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        // checkout
        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartArrayList.isEmpty())
                {
                    Toast.makeText(CartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    String orderTime = String.valueOf(binding.orderTime.getCheckedRadioButtonId());
                    String deliveryTime;
                    if (LocalTime.now().isBefore(LocalTime.parse("10:00"))) {
                        if (!orderTime.contentEquals(String.valueOf(binding.orderTime12.getId()))) {
                            Toast.makeText(CartActivity.this, "delivery must be at 12pm", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        deliveryTime = "12pm";
                    } else if (LocalTime.now().isBefore(LocalTime.parse("13:00"))) {
                        if (!orderTime.contentEquals(String.valueOf(binding.orderTime3.getId()))) {
                            Toast.makeText(CartActivity.this, "delivery must be at 3pm", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        deliveryTime = "3pm";
                    } else {
                        Toast.makeText(CartActivity.this, "cannot deliver currently", Toast.LENGTH_SHORT).show();
                    }

                    String orderPlace = String.valueOf(binding.orderPlace.getCheckedRadioButtonId());

                    if (orderPlace.contentEquals(String.valueOf(binding.orderPlace3.getId()))) {
                        orderPlace = "gate 3";
                    } else {
                        orderPlace = "gate 4";
                    }


                    // send orderTime, orderPlace and cartArrayList to the app for restaurants

                    cartArrayList.clear();
                    myRef.child(loginId).removeValue();

                    DatabaseReference myRef2 = database.getReference("Order");

                    String orderNb = generateOrderNumber();
                    myRef2.child(loginId).child(orderNb).setValue("pending");

                    Intent i = new Intent(CartActivity.this, HistoryActivity.class);
                    i.putExtra("LoginId", loginId);
                    startActivity(i);
                    finish();
                }
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
        Intent i = new Intent(CartActivity.this, CartActivity.class);;
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_cart: {
                break;
            }
            case R.id.nav_restaurants: {
                i = new Intent(CartActivity.this, MainActivity.class);
                break;
            }
            case R.id.nav_history: {
                i = new Intent(CartActivity.this, HistoryActivity.class);
                break;
            }
            case R.id.nav_profile: {
                i = new Intent(CartActivity.this, ProfileActivity.class);
                break;
            }
            case R.id.nav_logout: {
                i = new Intent(CartActivity.this, LoginActivity.class);
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

    public String generateOrderNumber() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

}