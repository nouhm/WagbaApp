package com.example.wagbaapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ProfileDao {

    @Query("DELETE FROM profile_table")
    void deleteAll();

    @Query("SELECT id from profile_table")
    String getMid();

    @Query("SELECT phoneNb from profile_table")
    String getMphonenb();

    @Query("SELECT name from profile_table")
    String getMname();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Profile profile);
}
