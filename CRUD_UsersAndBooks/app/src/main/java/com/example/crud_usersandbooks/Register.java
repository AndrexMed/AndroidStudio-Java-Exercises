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
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    TextView tvMessage;
    EditText etUser, etName,etEmail,etPassword;
    CheckBox btnStatus;
    Button btnRegister, btnDelete, btnSearch, btnBack, btnUpdate;
    String userStatus, userFounded, userToUpdate;

    DbConnection dbase = new DbConnection(this, "dbusers",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btnRegister);
        btnSearch = findViewById(R.id.btnSearch);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);
        btnUpdate = findViewById(R.id.btnUpdate);

        btnStatus = findViewById(R.id.btnStatus);

        tvMessage = findViewById(R.id.tvMessage);

        etUser = findViewById(R.id.etUser);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataRegisterValidation();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userToDelete = etUser.getText().toString();

                if(userToDelete.isEmpty()){
                    tvMessage.setTextColor(Color.RED);
                    tvMessage.setText("Id to delete is empty!");
                }else {
                    DeleteUser(userToDelete);
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userToSearch = etUser.getText().toString();

                if(userToSearch.isEmpty()){
                    tvMessage.setTextColor(Color.RED);
                    tvMessage.setText("Id to search is empty!");
                }else {
                    SearchUser(userToSearch);
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUpdateValidation();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackToHome();
            }
        });

    }//Fin OnCreate

    private void SearchUser(String userToSearch) {

        SQLiteDatabase db =  dbase.getReadableDatabase();

        String query = "SELECT idUser,nameUser, emailUser, password, status FROM Users WHERE idUser = '"+userToSearch+"'";

        Cursor cUser = db.rawQuery(query, null);

        if(cUser.moveToFirst()){

            userFounded = userToSearch;

            etName.setText(cUser.getString(1));
            etEmail.setText(cUser.getString(2));
            etPassword.setText(cUser.getString(3));

            if(cUser.getString(4).equals("1")){
                btnStatus.setChecked(true);
            }else{
                btnStatus.setChecked(false);
            }

            tvMessage.setTextColor(Color.GREEN);
            tvMessage.setText("User founded (200)");
        }else{
            tvMessage.setTextColor(Color.RED);
            tvMessage.setText("User not found (404)");
        }
        db.close();
    }

    private void BackToHome() {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }

    private void DeleteUser(String userToDelete) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Register.this);
        alertDialogBuilder.setMessage("¿ Are you right ?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        String userNameToDelete = etUser.getText().toString();

                        SQLiteDatabase db = dbase.getReadableDatabase();

                        String query= "SELECT idUser FROM Users WHERE idUser = '"+userNameToDelete+"'";

                        Cursor cUser = db.rawQuery(query,null);

                            if (cUser.moveToFirst()){

                                    db.execSQL("DELETE FROM Users WHERE idUser = '"+userNameToDelete+"'");

                                    tvMessage.setTextColor(Color.GREEN);
                                    tvMessage.setText("User"+userNameToDelete+" has been eliminated.");

                            }
                            else{
                                tvMessage.setTextColor(Color.RED);
                                tvMessage.setText("User to delete not found (404)");
                            }

                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(Register.this, "You have been cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        //----------------------------------------------
        /*SQLiteDatabase db = dbase.getReadableDatabase();

        String query= "SELECT idUser FROM Users WHERE idUser='"+userToDelete+"'";

        Cursor cUser = db.rawQuery(query,null);

            if (cUser.moveToFirst()){ //Lo Encuentra

                    String queryDelete = "DELETE FROM Users WHERE idUser = '"+userToDelete+"'";

                    db.execSQL(queryDelete);

                    tvMessage.setTextColor(Color.GREEN);
                    tvMessage.setText("User ("+userToDelete+") Is delete ...");
            }
            else{
                tvMessage.setTextColor(Color.RED);
                tvMessage.setText("User to delete not found.");
            }
        db.close();*/
    }

    private void DataRegisterValidation() {

        boolean isChecked = btnStatus.isChecked();

        if (isChecked){
            userStatus = "1";
        }else{
            userStatus = "0";
        }
        String idUser = etUser.getText().toString();
        String userName = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(idUser.isEmpty() || userName.isEmpty() || email.isEmpty() || password.isEmpty()){
            tvMessage.setTextColor(Color.RED);
            tvMessage.setText("All fields are required!");
        }else{
            UserRegister(idUser, userName, email, password, userStatus);
        }
    }

    private void UserRegister(String idUser, String userName, String email, String password, String userStatus) {

        SQLiteDatabase db =  dbase.getReadableDatabase();

        String query = "SELECT idUser FROM Users WHERE idUSer = '"+idUser+"'";

        Cursor cUser = db.rawQuery(query,null);

        if(!cUser.moveToFirst()){

            SQLiteDatabase dbw = dbase.getWritableDatabase();

            try {

                ContentValues cvUser = new ContentValues();
                cvUser.put("idUser",idUser );
                cvUser.put("nameUser", userName);
                cvUser.put("emailUser", email);
                cvUser.put("password", password);
                cvUser.put("status", userStatus);

                dbw.insert("Users",null,cvUser);
                dbw.close();
                tvMessage.setTextColor(Color.GREEN);
                tvMessage.setText("Successfully registered user...");

            }catch(Exception ex){
                Toast.makeText(this,"Error: "+ex.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }else{
            tvMessage.setTextColor(Color.RED);
            tvMessage.setText("The user already exists");
        }
        db.close();
    }

    private void DataUpdateValidation() {

        boolean isChecked = btnStatus.isChecked();

        if (isChecked){
            userStatus = "1";
        }else{
            userStatus = "0";
        }
        String idUser = etUser.getText().toString();
        String userName = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(idUser.isEmpty() || userName.isEmpty() || email.isEmpty() || password.isEmpty()){
            tvMessage.setTextColor(Color.RED);
            tvMessage.setText("All fields are required!");
        }else{
            userToUpdate = etUser.getText().toString();
            UserUpdate(userToUpdate, userName, email, password, userStatus);
        }
    }

    private void UserUpdate(String idUser, String userName, String email, String password, String userStatus) {

        SQLiteDatabase dbw = dbase.getWritableDatabase();

        if(userFounded.equals(userToUpdate)){
            String query = "UPDATE Users SET nameUser = '"+userName+"', emailUser = '"+email+"',"
                            + "password = '"+password+"', status = '"+userStatus+"'";

            dbw.execSQL(query);
            tvMessage.setTextColor(Color.GREEN);
            tvMessage.setText("User "+idUser+", update sucessfully...");
        }else{
            //Instanciar la clase SQLLiteDatabase en modo lectura, basado
            SQLiteDatabase db =  dbase.getReadableDatabase();

            //Generar variable que contendra la instruccion SELECT para recuperar el registro con filtro de username
            String query = "SELECT idUser FROM Users WHERE idUser = '"+idUser+"'";

            //Generar tabla cursor con los datos que retorna la istancia. select en la variable query
            Cursor cUser = db.rawQuery(query, null);

            if(!cUser.moveToFirst()){
                String query1 = "UPDATE Users SET idUser = '"+idUser+"', nameUser = '"+userName+"', emailUser = '"+email+"', password = '"+password+"', status = '"+userStatus+"' WHERE idUser = '"+userFounded+"'";

                dbw.execSQL(query1);
                tvMessage.setTextColor(Color.GREEN);
                tvMessage.setText("Usuario "+idUser+", actualizado correctamente...");
            }else{
                tvMessage.setTextColor(Color.RED);
                tvMessage.setText("Usuario "+idUser+", esta asignado a otro. Intentelo con otro");
            }
        db.close();
        }
        dbw.close();
    }

}