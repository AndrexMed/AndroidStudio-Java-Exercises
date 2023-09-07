package com.example.appusers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // Instanciar ids de xml
    EditText etusername,etfullname,etemail, etpassword, etrole;
    ImageButton btnsave, btnsearch, btnlist, btnUpdate, btnDelete, btnClear;
    TextView tvmessage;
    // Instanciar la clase de la base de datos para actualizar la info del usuario (CRUD)
    clsbd dbase = new clsbd(this, "dbusers",null,1);

    String userNameFounded = "";
    String userToUpdate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Referenciar los objetos (controles) con sus ids respectivos
        etusername = findViewById(R.id.etusername);
        etfullname = findViewById(R.id.etfullname);
        etemail = findViewById(R.id.etemail);
        etpassword = findViewById(R.id.etpassword);
        etrole = findViewById(R.id.etrole);
        tvmessage = findViewById(R.id.tvmessage);
        btnsave = findViewById(R.id.btnsave);
        btnsearch = findViewById(R.id.btnsearch);
        btnlist = findViewById(R.id.btnlist);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnClear = findViewById(R.id.btnClear);
        // Eventos
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etusername.getText().toString();
                String fullname = etfullname.getText().toString();
                String email = etemail.getText().toString();
                String password = etpassword.getText().toString();
                String role = etrole.getText().toString();
                if (!username.isEmpty() && !fullname.isEmpty()
                        && !email.isEmpty() && !password.isEmpty() && !role.isEmpty()){
                    saveUser(username, fullname, email, password, role);
                }
                else{
                    tvmessage.setTextColor(Color.RED);
                    tvmessage.setText("Debe ingresar todos los datos solicitados");
                }
            }
        });

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verificar que el username se haya ingresado.
                try {
                    searchUser(etusername.getText().toString());
                }catch (Exception ex){

                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userToUpdate = etusername.getText().toString();

                String fullname = etfullname.getText().toString();
                String email = etemail.getText().toString();
                String password = etpassword.getText().toString();
                String role = etrole.getText().toString();

                update(userToUpdate, fullname, email, password, role);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameToDelete = etusername.getText().toString();
                delete(userNameToDelete);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });

    }//Fin OnCreate

    private void clearData() {
        etusername.setText("");
        etfullname.setText("");
        etemail.setText("");
        etpassword.setText("");
        etrole.setText("");
    }

    private void delete(String userNameToDelete) {
        SQLiteDatabase db = dbase.getWritableDatabase();
        String query = "DELETE FROM user WHERE username = '"+userNameToDelete+"'";

        Cursor cUser = db.rawQuery(query,null);

        if(cUser.moveToFirst()){

        }
    }

    private void update(String updateUsername, String fullname, String email, String password, String role) {
        SQLiteDatabase dbw = dbase.getWritableDatabase();
        //Comprobar si se ha cambiado el etuserName
        if(userNameFounded.equals(userToUpdate)){
            String query = "UPDATE user SET fullname = '"+fullname+"', email = '"+email+"', password = '"+password+"', role = '"+role+"' WHERE username = '"+userNameFounded+"'";
            dbw.execSQL(query);
            tvmessage.setTextColor(Color.GREEN);
            tvmessage.setText("Usuario "+updateUsername+", actualizado correctamente...");
        }else{
            //Instanciar la clase SQLLiteDatabase en modo lectura, basado
            SQLiteDatabase db =  dbase.getReadableDatabase();

            //Generar variable que contendra la instruccion SELECT para recuperar el registro con filtro de username
            String query = "SELECT username FROM user WHERE username = '"+updateUsername+"'";

            //Generar tabla cursor con los datos que retorna la istancia. select en la variable query
            Cursor cUser = db.rawQuery(query,null);

            //Verificar si la tabla cursor tiene al menos un registro
            if(!cUser.moveToFirst()){ //No lo Encontro
                String query1 = "UPDATE user set username = '"+updateUsername+"', fullname = '"+fullname+"', password = '"+password+"', role = '"+role+"' WHERE username = '"+userNameFounded+"'";

                dbw.execSQL(query1, null);
                tvmessage.setTextColor(Color.GREEN);
                tvmessage.setText("Usuario "+updateUsername+", actualizado correctamente...");
            }else{
                tvmessage.setTextColor(Color.RED);
                tvmessage.setText("Usuario "+updateUsername+", esta asignado a otro. Intentelo con otro");
            }
            //db.close();
        }
        //dbw.close();
    }

    private void searchUser(String username) {
        //Instanciar la clase SQLLiteDatabase en modo lectura, basado
        SQLiteDatabase db =  dbase.getReadableDatabase();

        //Generar variable que contendra la instruccion SELECT para recuperar el registro con filtro de username
        String query = "SELECT username,fullname, password, email, role FROM user WHERE username = '"+username+"'";

        //Generar tabla cursor con los datos que retorna la istancia. select en la variable query
        Cursor cUser = db.rawQuery(query,null);

        //Verificar si la tabla cursor tiene al menos un registro
        if(cUser.moveToFirst()){
            userNameFounded = username; // Variable para almacenar el usuario encontrado
            //llenar el formulario con los datos encontrados
            //etusername.setText(cUser.getString(0));
            etfullname.setText(cUser.getString(1));
            etpassword.setText(cUser.getString(2));
            etemail.setText(cUser.getString(3));
            etrole.setText(cUser.getString(4));

            tvmessage.setText("Usuario encontrado!!");
        }else{
            tvmessage.setTextColor(Color.RED);
            tvmessage.setText("Nombre de usuario NO Existente.!");
        }
        db.close();
    }

    private void saveUser(String username, String fullname, String email, String password, String role) {
        //-------------------------------------------------------------------------------------------------
        //Instanciar la clase SQLLiteDatabase en modo lectura, basado en el objeto dbase
        SQLiteDatabase db =  dbase.getReadableDatabase();

        //Generar variable que contendra la instruccion SELECT para recuperar el registro con filtro de username
        String query = "SELECT username,fullname, password, email, role FROM user WHERE username = '"+username+"'";

        //Generar tabla cursor con los datos que retorna la istancia. select en la variable query
        Cursor cUser = db.rawQuery(query,null);

        //Verificar si la tabla cursor tiene al menos un registro
        if(!cUser.moveToFirst()){

            // Instanciar la clase SQLiteDatabase, en modo writable
            SQLiteDatabase dbw = dbase.getWritableDatabase();
            // Manejo de excepciones a través try
            try {
                // Se genera un objeto content values para crear una tabla "temporal" con los datos del user
                ContentValues cvUser = new ContentValues();
                cvUser.put("username",username );
                cvUser.put("fullname", fullname);
                cvUser.put("password", password);
                cvUser.put("email", email);
                cvUser.put("role", role);
                // ejecutar la instrucción para pasar los datos de la tebla content values a la tabla fisica
                dbw.insert("user",null,cvUser);
                dbw.close();
                tvmessage.setTextColor(Color.GREEN);
                tvmessage.setText("Usuario guardado correctamente ...");

            }
            catch (Exception e){
                Toast.makeText(this,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }else{
            tvmessage.setTextColor(Color.RED);
            tvmessage.setText("Nombre de usuario YA existe. Intentelo con otro");
        }
        db.close();
    }



}