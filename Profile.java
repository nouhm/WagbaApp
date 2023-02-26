package com.example.wagbaapplication;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "profile_table")
public class Profile {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String mid;

    @ColumnInfo(name = "name")
    private String mname;

    @ColumnInfo(name = "phoneNb")
    private String mphonenb;

    public Profile(@NonNull String mid, String mname, String mphonenb) {
        this.mid = mid;
        this.mname = mname;
        this.mphonenb = mphonenb;
    }

    public String getMid(){return this.mid;}

    public void setMname(String name){this.mname = name;}

    public String getMname(){return this.mname;}

    public void setMphonenb(String phonenb){this.mphonenb = phonenb;}

    public String getMphonenb(){return this.mphonenb;}

}
