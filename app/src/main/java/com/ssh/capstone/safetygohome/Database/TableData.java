package com.ssh.capstone.safetygohome.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TableData extends SQLiteOpenHelper {

    public TableData(@Nullable Context context) {
        super(context, "database/ssh.db", null, 1);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table if not exists user_info(user_no INT PRIMARY KEY," +
                "user_name CHAR(20), user_num CHAR(20));"); //황찬우 수정
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
