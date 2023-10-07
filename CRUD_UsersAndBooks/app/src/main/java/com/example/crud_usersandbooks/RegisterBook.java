package com.example.crud_usersandbooks;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterBook extends AppCompatActivity {

    TextView tvbookMessage;
    EditText etidbook, etbookname, etcost;
    CheckBox btnbookStatus;
    ImageButton btnbookRegister, btnbookSearch, btnbookUpdate, btnbookDelete, btnbookBack;
    String bookStatus, bookFounded, bookToUpdate;

    DbConnection dbase = new DbConnection(this, "dbusers",null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_book);

        btnbookRegister = findViewById(R.id.btnbookRegister);
        btnbookSearch = findViewById(R.id.btnbookSearch);
        btnbookUpdate = findViewById(R.id.btnbookUpdate);
        btnbookDelete = findViewById(R.id.btnbookDelete);
        btnbookBack =   findViewById(R.id.btnbookBack);

        btnbookStatus = findViewById(R.id.btnbookStatus);

        tvbookMessage = findViewById(R.id.tvbookMessage);

        etidbook = findViewById(R.id.etidbook);
        etbookname = findViewById(R.id.etbookname);
        etcost = findViewById(R.id.etcost);

        btnbookRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookRegisterValidation();
            }
        });

        btnbookDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookToDelete = etidbook.getText().toString();

                if(bookToDelete.isEmpty()){
                    tvbookMessage.setTextColor(Color.RED);
                    tvbookMessage.setText("Idbook to delete is empty!");
                }else {
                    DeleteUser(bookToDelete);
                }
            }
        });
        btnbookSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookToSearch = etidbook.getText().toString();

                if (bookToSearch.isEmpty()){
                    tvbookMessage.setTextColor(Color.RED);
                    tvbookMessage.setText("IdBook to search is empty!");
                }else{
                    searchBook(bookToSearch);
                }
            }
        });

        btnbookUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookUpdateValidation();
            }
        });

        btnbookBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackToHome();
            }
        });
    }//Fin Oncreate

    private void BookUpdateValidation() {

        boolean isChecked = btnbookStatus.isChecked();

        if (isChecked){
            bookStatus = "1";
        }else{
            bookStatus = "0";
        }
        String idBook = etidbook.getText().toString();
        String nameBook = etbookname.getText().toString();
        String coste = etcost.getText().toString();

        if(idBook.isEmpty() || nameBook.isEmpty() || coste.isEmpty()){
            tvbookMessage.setTextColor(Color.RED);
            tvbookMessage.setText("All fields are required!");
        }else{
            bookToUpdate = etidbook.getText().toString();
            BookUpdate(bookToUpdate, nameBook, coste, bookStatus);
        }
    }

    private void BookUpdate(String idBook, String nameBook, String cost, String available) {

        SQLiteDatabase dbw = dbase.getWritableDatabase();

        if(bookFounded.equals(bookToUpdate)){

            String query = "UPDATE Books SET idBook = '"+idBook+"', nameBook = '"+nameBook+"',"
                    + "coste = '"+cost+"', available = '"+available+"' WHERE idBook = '"+idBook+"'";

            dbw.execSQL(query);
            tvbookMessage.setTextColor(Color.GREEN);
            tvbookMessage.setText("Book with id: "+idBook+", update sucessfully...");
        }else{
            //Instanciar la clase SQLLiteDatabase en modo lectura, basado
            SQLiteDatabase db =  dbase.getReadableDatabase();

            //Generar variable que contendra la instruccion SELECT para recuperar el registro con filtro de username
            String query = "SELECT idBook FROM Books WHERE idBook = '"+idBook+"'";

            //Generar tabla cursor con los datos que retorna la istancia. select en la variable query
            Cursor cUser = db.rawQuery(query, null);

            if(!cUser.moveToFirst()){
                String query1 = "UPDATE Books SET idBook = '"+idBook+"', nameBook = '"+nameBook+"', coste = '"+cost+"', available = '"+available+"' WHERE idBook = '"+idBook+"'";

                dbw.execSQL(query1);
                tvbookMessage.setTextColor(Color.GREEN);
                tvbookMessage.setText("Book "+idBook+", updated successfully...");
            }else{
                tvbookMessage.setTextColor(Color.RED);
                tvbookMessage.setText("Book "+idBook+", is assigned to another. Try it with another");
            }
            db.close();
        }
        dbw.close();
    }

    private void BackToHome() {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }

    private void searchBook(String bookToSearch) {

        SQLiteDatabase db =  dbase.getReadableDatabase();

        String query = "SELECT idBook, nameBook, coste, available FROM Books WHERE idbook = '"+bookToSearch+"'";

        Cursor cUser = db.rawQuery(query, null);

        if(cUser.moveToFirst()){

            bookFounded = bookToSearch;

            etbookname.setText(cUser.getString(1));
            etcost.setText(cUser.getString(2));

            if(cUser.getString(3).equals("1")){
                btnbookStatus.setChecked(true);
            }else{
                btnbookStatus.setChecked(false);
            }

            tvbookMessage.setTextColor(Color.GREEN);
            tvbookMessage.setText("Book founded (200)");
        }else{
            tvbookMessage.setTextColor(Color.RED);
            tvbookMessage.setText("Book not found (404)");
        }
        db.close();
    }

    private void DeleteUser(String bookToDelete) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterBook.this);
        alertDialogBuilder.setMessage("¿ are you sure ?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        String idBookToDelete = etidbook.getText().toString();

                        SQLiteDatabase db = dbase.getReadableDatabase();

                        String query= "SELECT idBook FROM Books WHERE idBook = '"+idBookToDelete+"'";

                        Cursor cUser = db.rawQuery(query,null);

                        if(cUser.moveToFirst()){

                            db.execSQL("DELETE FROM Books WHERE idBook = '"+idBookToDelete+"'");

                            tvbookMessage.setTextColor(Color.GREEN);
                            tvbookMessage.setText("Book with id: "+idBookToDelete+" has been eliminated.");

                        }else{
                            tvbookMessage.setTextColor(Color.RED);
                            tvbookMessage.setText("Book to delete not found (404)");
                        }

                    }
                });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(RegisterBook.this, "You have been cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void BookRegisterValidation() {

        boolean ischecked = btnbookStatus.isChecked();

        if (ischecked){
            bookStatus = "1";
        }else{
            bookStatus = "0";
        }
        String idBook = etidbook.getText().toString();
        String bookname = etbookname.getText().toString();
        String Cost = etcost.getText().toString();

        if (idBook.isEmpty() || bookname.isEmpty() || Cost.isEmpty()){
            tvbookMessage.setTextColor(Color.RED);
            tvbookMessage.setText("All fields are required");
        }else{
            bookRegister(idBook, bookname, Cost, bookStatus);
        }
    }

    private void bookRegister(String idBook, String bookname, String cost, String available) {
        SQLiteDatabase db =  dbase.getReadableDatabase();

        String query = "SELECT idBook FROM Books WHERE idBook = '"+idBook+"'";

        Cursor cUser = db.rawQuery(query,null);

        if(!cUser.moveToFirst()){
            SQLiteDatabase dbw = dbase.getWritableDatabase();

            try {
                ContentValues cvUser = new ContentValues();
                cvUser.put("idBook",idBook );
                cvUser.put("nameBook", bookname);
                cvUser.put("coste", cost);
                cvUser.put("available", available);

                dbw.insert("Books",null,cvUser);
                dbw.close();
                tvbookMessage.setTextColor(Color.GREEN);
                tvbookMessage.setText("Successfully registered Book...");
            }catch (Exception ex){
                Toast.makeText(this,"Error: "+ex.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }else{
            tvbookMessage.setTextColor(Color.RED);
            tvbookMessage.setText("The Book already exists");
        }
        db.close();
    }

}