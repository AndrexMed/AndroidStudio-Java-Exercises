package com.example.trabajofinal_mobiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText usuario,clave;

    String Usuario, Clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = findViewById(R.id.etusuario);
        clave = findViewById(R.id.etclave);
    }

    public void login(View view){

        Usuario = usuario.getText().toString();
        Clave = clave.getText().toString();

        if (Usuario.isEmpty() || Clave.isEmpty()){
            Toast.makeText(this, "Usuario y Contraseña Obligatorios", Toast.LENGTH_SHORT).show();
        }else{
            String usuario1 = "holamundo";
            String clave1 = "1234";

            if (Usuario.equals(usuario1) && Clave.equals(clave1)){

                Intent intent = new Intent(this, MainActivity2.class);
                startActivity(intent);

            }else{
                Toast.makeText(this, "Usuario o Clave Incorrecto", Toast.LENGTH_SHORT).show();
            }
        }

    }
}