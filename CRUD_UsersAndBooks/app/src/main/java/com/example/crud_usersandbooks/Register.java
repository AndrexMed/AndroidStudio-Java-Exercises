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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    TextView tvMessage;
    EditText etUser, etName,etEmail,etPassword;
    CheckBox btnStatus;
    Button btnRegister, btnDelete, btnBack;
    String userStatus;

    DbConnection dbase = new DbConnection(this, "dbusers",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btnRegister);
        btnDelete = findViewById(R.id.btnDelete);
        btnStatus = findViewById(R.id.btnStatus);
        btnBack = findViewById(R.id.btnBack);

        tvMessage = findViewById(R.id.tvMessage);

        etUser = findViewById(R.id.etUser);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    tvMessage.setText("Campos obligatorios!");
                }else{
                    UserRegister(idUser, userName, email, password, userStatus);
                }

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userToDelete = etUser.getText().toString();
                Delete(userToDelete);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackToHome();
            }
        });

    }//Fin OnCreate

    private void BackToHome() {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }

    private void Delete(String userToDelete) {

        SQLiteDatabase db = dbase.getReadableDatabase();

        String query= "SELECT idUser FROM Users WHERE idUser='"+userToDelete+"'";

        Cursor cUser = db.rawQuery(query,null);

            if (cUser.moveToFirst()){ //Lo Encuentra

                    String queryDelete = "DELETE FROM Users WHERE idUser = '"+userToDelete+"'";

                    db.execSQL(queryDelete);

                    tvMessage.setTextColor(Color.GREEN);
                    tvMessage.setText("Usuario ("+userToDelete+") Eliminado correctamente...");
            }
            else{
                tvMessage.setTextColor(Color.RED);
                tvMessage.setText("Nombre de usuario No existe. Intentelo con otro...");
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
                tvMessage.setText("Usuario guardado correctamente ...");

            }catch(Exception ex){
                Toast.makeText(this,"Error: "+ex.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }else{
            tvMessage.setTextColor(Color.RED);
            tvMessage.setText("Nombre de usuario YA existe. Intentelo con otro");
        }
        db.close();
    }

}