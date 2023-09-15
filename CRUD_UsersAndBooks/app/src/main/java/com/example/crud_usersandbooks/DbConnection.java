package com.example.crud_usersandbooks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbConnection extends SQLiteOpenHelper {

    String tblUsers = "CREATE TABLE Users(idUser text, nameUser text, emailUser text, password text, status text)";
    String tblBooks = "CREATE TABLE Books(idBook text, nameBook text, coste text, available text)";
    String tblRents = "CREATE TABLE Rents(idRent INTEGER PRIMARY KEY AUTOINCREMENT, idUser text, idBook text, date text)";

    public DbConnection(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Se ejecuta el contenido de la (s) variable (s) que permiten crear las tablas
        db.execSQL(tblUsers);
        db.execSQL(tblBooks);
        db.execSQL(tblRents);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se borra (n) todas las tablas y luego se regeneran
        db.execSQL("DROP TABLE Users");
        db.execSQL(tblUsers);

        db.execSQL("DROP TABLE Books");
        db.execSQL(tblBooks);

        db.execSQL("DROP TABLE Rents");
        db.execSQL(tblRents);
    }
}
