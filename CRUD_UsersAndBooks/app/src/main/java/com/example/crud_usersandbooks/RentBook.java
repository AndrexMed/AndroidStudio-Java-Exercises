package com.example.crud_usersandbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class RentBook extends AppCompatActivity {
    TextView tvNameBook, tvNameUser, tvCosteBook;
    EditText etIdBook, etIdUser;
    TextView tvMessage;
    Button btnSearch, btnBack, btnSave;

    DbConnection dbase = new DbConnection(this, "dbusers",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_book);

        etIdBook = findViewById(R.id.etIdBook);
        etIdUser = findViewById(R.id.etIdUser);

        btnSearch = findViewById(R.id.btnSearch);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        tvMessage = findViewById(R.id.tvMessage);
        tvNameBook = findViewById(R.id.tvNameBook);
        tvNameUser = findViewById(R.id.tvNameUser);
        tvCosteBook = findViewById(R.id.tvCosteBook);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idBook = etIdBook.getText().toString();
                String idUser = etIdUser.getText().toString();

                IsUserAndBookAvailableValidation(idBook, idUser);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoBack();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idBook = etIdBook.getText().toString();
                String idUser = etIdUser.getText().toString();

                saveRegisterRent(idBook, idUser);
            }
        });

    }

    /*private void saveRegisterRent(String idBook, String idUser) {

        SQLiteDatabase db =  dbase.getReadableDatabase();
        SQLiteDatabase db2 =  dbase.getReadableDatabase();

        String queryB = "SELECT idBook, nameBook, coste FROM Books WHERE idBook = '"+idBook+"'";
        String queryU = "SELECT idUser, nameUser FROM Users WHERE idUser = '"+idUser+"'";

        Cursor cBook = db.rawQuery(queryB, null);
        Cursor cUser = db2.rawQuery(queryU, null);

        if (cBook.moveToFirst() && cUser.moveToFirst()){

            SQLiteDatabase dbw = dbase.getWritableDatabase();

            try {

                String fechaNow = new Date().toString();

                ContentValues cvRent = new ContentValues();
                cvRent.put("idUser",idUser );
                cvRent.put("idBook", idBook);
                cvRent.put("date", fechaNow);

                dbw.insert("Rents",null,cvRent);
                dbw.close();
                tvMessage.setTextColor(Color.GREEN);
                tvMessage.setText("Successfully registered rent...");

            }catch (Exception ex){
                Toast.makeText(this,"Error: "+ex.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }else {
            tvMessage.setTextColor(Color.RED);
            tvMessage.setText("Ya tiene este libro asignado");
        }
    }*/

    private void saveRegisterRent(String idBook, String idUser) {
        SQLiteDatabase db = dbase.getReadableDatabase();

        String queryB = "SELECT idBook, nameBook, coste FROM Books WHERE idBook = '" + idBook + "'";
        String queryU = "SELECT idUser, nameUser FROM Users WHERE idUser = '" + idUser + "'";

        Cursor cBook = db.rawQuery(queryB, null);
        Cursor cUser = db.rawQuery(queryU, null);

        if (cBook.moveToFirst() && cUser.moveToFirst()) {
            SQLiteDatabase dbw = dbase.getWritableDatabase();

            // Verificar si ya existe un registro con el mismo idBook e idUser
            String checkQuery = "SELECT COUNT(*) FROM Rents WHERE idBook = ? AND idUser = ?";
            Cursor checkCursor = dbw.rawQuery(checkQuery, new String[]{idBook, idUser});
            checkCursor.moveToFirst();
            int existingCount = checkCursor.getInt(0);
            checkCursor.close();

            if (existingCount == 0) {
                try {
                    String fechaNow = new Date().toString();

                    ContentValues cvRent = new ContentValues();
                    cvRent.put("idUser", idUser);
                    cvRent.put("idBook", idBook);
                    cvRent.put("date", fechaNow);

                    dbw.insert("Rents", null, cvRent);
                    dbw.close();
                    tvMessage.setTextColor(Color.GREEN);
                    tvMessage.setText("Registro de préstamo exitoso...");
                } catch (Exception ex) {
                    Toast.makeText(this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                // Ya existe un registro con el mismo libro asignado al usuario.
                tvMessage.setTextColor(Color.RED);
                tvMessage.setText("Ya tiene este libro asignado");
            }
        } else {
            tvMessage.setTextColor(Color.RED);
            tvMessage.setText("Error aquí");
        }
    }

    //---------------------------------------------------------------------------
    private void GoBack() {
        Intent back = new Intent(this, MainActivity.class);
        startActivity(back);
    }

    private void IsUserAndBookAvailableValidation(String idBook, String idUser) {

        if (idUser.isEmpty() || idBook.isEmpty()){
            tvMessage.setTextColor(Color.RED);
            tvMessage.setText("Ids obligatorios");
        }else{
            boolean userValid = UserValid(idUser);
            boolean bookValid = BookValid(idBook);

            if (userValid && bookValid){
                    //SaveRent(idUser, idBook);
                tvMessage.setTextColor(Color.GREEN);
                tvMessage.setText("It's Ok");

                PrintDataInTable(idBook, idUser);
            }else{
                if (!userValid){
                    tvMessage.setTextColor(Color.RED);
                    tvMessage.setText("User no registered or disabled");
                }
                if (!bookValid){
                    tvMessage.setTextColor(Color.RED);
                    tvMessage.setText("Book no registered or disabled");
                }
            }
        }
    }

    private void PrintDataInTable(String idBook, String idUser) {

        SQLiteDatabase db =  dbase.getReadableDatabase();
        SQLiteDatabase db2 =  dbase.getReadableDatabase();

        String queryB = "SELECT idBook, nameBook, coste FROM Books WHERE idBook = '"+idBook+"'";
        String queryU = "SELECT idUser, nameUser FROM Users WHERE idUser = '"+idUser+"'";

        Cursor cBook = db.rawQuery(queryB, null);
        Cursor cUser = db2.rawQuery(queryU, null);

        if (cBook.moveToFirst() && cUser.moveToFirst()){
            tvNameBook.setText(cBook.getString(1));
            tvNameUser.setText(cUser.getString(1));
            tvCosteBook.setText(cBook.getString(2));
        }else{
            tvMessage.setTextColor(Color.RED);
            tvMessage.setText("Error aqui!");
        }

        cBook.close();
        cUser.close();
    }

    private boolean UserValid(String idUser){

        SQLiteDatabase db =  dbase.getReadableDatabase();

        String query = "SELECT idUser FROM Users WHERE idUser = '"+idUser+"' AND status = '1'";

        Cursor cUser = db.rawQuery(query, null);

        if (cUser.moveToFirst()){
            return true;
        }else {
            return false;
        }
    }

    private boolean BookValid(String idBook){

        SQLiteDatabase db =  dbase.getReadableDatabase();

        String query = "SELECT idBook FROM Books WHERE idBook = '"+idBook+"' AND available = '1'";

        Cursor cUser = db.rawQuery(query, null);

        if (cUser.moveToFirst()){
            return true;
        }else {
            return false;
        }
    }

}