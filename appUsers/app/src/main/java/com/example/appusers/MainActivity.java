package com.example.appusers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    // Instanciar ids de xml
    Spinner spRole;
    String[] spRoles = {"Seleccione un rol", "Admin", "Usuario"};

    EditText etusername,etfullname,etemail, etpassword;
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
        spRole = findViewById(R.id.spRole);
        tvmessage = findViewById(R.id.tvmessage);
        btnsave = findViewById(R.id.btnsave);
        btnsearch = findViewById(R.id.btnsearch);
        btnlist = findViewById(R.id.btnlist);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnClear = findViewById(R.id.btnClear);

        spRole.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, spRoles));

        // Eventos
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etusername.getText().toString();
                String fullname = etfullname.getText().toString();
                String email = etemail.getText().toString();
                String password = etpassword.getText().toString();
                String role = spRole.getSelectedItem().toString();
                if (!username.isEmpty() && !fullname.isEmpty()
                        && !email.isEmpty() && !password.isEmpty() && !role.isEmpty()){

                    Pattern patEmail = Patterns.EMAIL_ADDRESS;

                    if (patEmail.matcher(email).matches()){

                        if (!role.equals("Seleccione un rol")){
                            saveUser(username, fullname, email, password, role);
                        }else{
                            tvmessage.setTextColor(Color.RED);
                            tvmessage.setText("Seleccione un rol valido!");
                        }
                    }else{
                        etemail.setError("Email invalido");
                    }

                    //saveUser(username, fullname, email, password, role);
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
                String role = spRole.getSelectedItem().toString();

                update(userToUpdate, fullname, email, password, role);
            }
        });

        /*btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameToDelete = etusername.getText().toString();
                delete(userNameToDelete);
            }
        });*/

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage("Eliminación de Contactos");
                alertDialogBuilder.setPositiveButton("Sí",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                /*SQLiteDatabase obde = dbase.getWritableDatabase();
                                obde.execSQL("DELETE FROM user WHERE username = '"+etusername.getText().toString()+"'");
                                Toast.makeText(getApplicationContext(),"Contacto Eliminado correctamente...",Toast.LENGTH_SHORT).show();*/

                                String userNameToDelete = etusername.getText().toString();
                                SQLiteDatabase db = dbase.getReadableDatabase();

                                String query= "SELECT role FROM user WHERE username='"+userNameToDelete+"'";

                                Cursor cUser = db.rawQuery(query,null);

                                if(cUser.moveToFirst()){
                                    if (cUser.moveToFirst()){//lo encontro
                                        if(!cUser.getString(0).equals("Admin")){
                                            db.execSQL("DELETE FROM user WHERE username='" + userNameToDelete + "'");

                                            tvmessage.setTextColor(Color.GREEN);
                                            tvmessage.setText("Usuario"+userNameToDelete+" Eliminado correctamente...");

                                            clearData();
                                        }else{
                                            tvmessage.setTextColor(Color.RED);
                                            tvmessage.setText("No se puede eliminar un usuario administrador!");
                                        }
                                    }
                                    else{
                                        tvmessage.setTextColor(Color.RED);
                                        tvmessage.setText("nombre de usuario No existe. Intentelo con otro...");
                                    }
                                }
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(MainActivity.this, "Operacion cancelada", Toast.LENGTH_SHORT).show();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                /*SQLiteDatabase obde = odb.getWritableDatabase();
                obde.execSQL("DELETE FROM Contacto WHERE Nombres = '"+etnombres.getText().toString()+"'");
                Toast.makeText(getApplicationContext(),"Contacto Eliminado correctamente...",Toast.LENGTH_SHORT).show();
                */


            }
        });

        btnlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "Lista de Usuarios";
                Intent listUsers = new Intent(getApplicationContext(), UserList.class);
                listUsers.putExtra("mtitle", title);
                startActivity(listUsers);

                //startActivity(new Intent(getApplicationContext(), UserList.class));
            }
        });

    }//Fin OnCreate

    private void userList() {
        Intent userList = new Intent(this, UserList.class);
        startActivity(userList);
    }

    private void clearData(){
        etfullname.setText("");
        etusername.setText("");
        etemail.setText("");
        etpassword.setText("");

        //spRole.(""); Pendiente
    }
    private void delete(String userNameToDelete) {
        SQLiteDatabase db = dbase.getReadableDatabase();

        String query= "SELECT role FROM user WHERE username='"+userNameToDelete+"'";

        Cursor cUser = db.rawQuery(query,null);

        if(cUser.moveToFirst()){
            if (cUser.moveToFirst()){//lo encontro
                if(!cUser.getString(0).equals("Admin")){
                    db.execSQL("DELETE FROM user WHERE username='" + userNameToDelete + "'");

                    tvmessage.setTextColor(Color.GREEN);
                    tvmessage.setText("Usuario"+userNameToDelete+" Eliminado correctamente...");

                    clearData();
                }else{
                    tvmessage.setTextColor(Color.RED);
                    tvmessage.setText("No se puede eliminar un usuario administrador!");
                }
            }
            else{
                tvmessage.setTextColor(Color.RED);
                tvmessage.setText("nombre de usuario No existe. Intentelo con otro...");
            }
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
            Cursor cUser = db.rawQuery(query, null);

            //Verificar si la tabla cursor tiene al menos un registro
            if(!cUser.moveToFirst()){ //No lo Encontro
                String query1 = "UPDATE user SET username = '"+updateUsername+"', fullname = '"+fullname+"', password = '"+password+"', role = '"+role+"' WHERE username = '"+userNameFounded+"'";

                dbw.execSQL(query1);
                tvmessage.setTextColor(Color.GREEN);
                tvmessage.setText("Usuario "+updateUsername+", actualizado correctamente...");
            }else{
                tvmessage.setTextColor(Color.RED);
                tvmessage.setText("Usuario "+updateUsername+", esta asignado a otro. Intentelo con otro");
            }
            db.close();
        }
        dbw.close();
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
            /*int index;
            if (cUser.getString(4).equals("Admin")){
                index = 1;
            }else{
                index = 2;
            }*/
            int index = cUser.getString(4).equals("Admin") ? 1 : 2; //Operador ternario
            spRole.setSelection(index);

            //spRole.setText(cUser.getString(4));  Pendiente

            tvmessage.setText("Usuario encontrado!!");
            tvmessage.setTextColor(Color.GREEN);
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