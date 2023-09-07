package com.example.classtwo_mobilestwo_gioalzate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ClassBD extends SQLiteOpenHelper {

    String tblUser = "CREATE TABLE user(username text, fullname text, email text, password text, rol text)";
    public ClassBD(Context context,  String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE user");
        db.execSQL(tblUser);
    }
}
