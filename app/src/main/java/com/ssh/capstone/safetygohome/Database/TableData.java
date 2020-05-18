package com.ssh.capstone.safetygohome.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TableData extends SQLiteOpenHelper {

    public TableData(@Nullable Context context) {
        super(context, "ssh.db", null, 1);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table if not exists user_info(user_no INT PRIMARY KEY," +
                "user_name CHAR(20), user_num CHAR(20));"); //황찬우 수정

        db.execSQL("create table if not exists cctv(num INT PRIMARY KEY, address CHAR(100), latitude REAL, longitude REAL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
