package com.example.wagbaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.wagbaapplication.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.text.Regex;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    String finalInputUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Login");

        ProfileRoomDatabase profileRoomDatabase = ProfileRoomDatabase.getDatabase(getApplicationContext());
        ProfileDao profileDao = profileRoomDatabase.profileDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                profileDao.deleteAll();
            }
        }).start();

        binding.loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputUsername = binding.username.getText().toString().trim();
                String inputPassword = binding.password.getText().toString();


                if (!isEmailValid(inputUsername)) {
                    Toast.makeText(LoginActivity.this, "Email is invalid", Toast.LENGTH_SHORT).show();
                }
                // CHECK IF USERNAME AND PASSWORD MATCHES DB
                else {
                    inputUsername = inputUsername.substring(0, inputUsername.length()-15);
                    Log.d("login", inputUsername);
                    finalInputUsername = inputUsername;
                    myRef.child(inputUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Log.d("login", "email found in db");
                                //check password matches
                                if (snapshot.getValue(String.class).equals(inputPassword)) {
                                    //start main activity
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    i.putExtra("LoginId", finalInputUsername);
                                    startActivity(i);
                                    finish();
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                //user does not exist, create user
                                Log.d("login", "email not found in db");
                                myRef.child(finalInputUsername).setValue(inputPassword);
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                i.putExtra("LoginId", finalInputUsername);
                                startActivity(i);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Profile profile = new Profile(finalInputUsername, null, null);
                        profileDao.insert(profile);
                    }
                }).start();

            }
        });

    }

    public boolean isEmailValid(String email)
    {
        return email.endsWith("@eng.asu.edu.eg");
    }

}