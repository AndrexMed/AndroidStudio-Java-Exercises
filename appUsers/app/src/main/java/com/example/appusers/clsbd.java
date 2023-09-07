package com.example.appusers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class clsbd extends SQLiteOpenHelper {
    String tblUser = "CREATE TABLE user(username text, fullname text, password text, email text, role text)";
    public clsbd(Context context,  String name,  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Se ejecuta el contenido de la (s) variable (s) que permiten crear las tablas
        db.execSQL(tblUser);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se borra (n) todas las tablas y luego se regeneran
      db.execSQL("DROP TABLE user");
      db.execSQL(tblUser);

    }
}
