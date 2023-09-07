package com.example.classtwo_mobilestwo_gioalzate;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    EditText etUser,etFullName, etEmail, etPassword, etRol;
    ImageButton btnSave,btnSearch,btnList;
    TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUser = findViewById(R.id.etUser);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etRol = findViewById(R.id.etRol);
        tvMessage = findViewById(R.id.tvMessage);

        btnSave = findViewById(R.id.btnSave);
        btnSearch = findViewById(R.id.btnSearch);
        btnList = findViewById(R.id.btnList);

        //Eventos
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserName = etUser.getText().toString();
                String FullName = etFullName.getText().toString();
                String Email = etEmail.getText().toString();
                String Password = etPassword.getText().toString();
                String Rol = etRol.getText().toString();

                if(!UserName.isEmpty() && !FullName.isEmpty() && !Email.isEmpty() && !Password.isEmpty()){
                    //saveUser(UserName, FullName, Email, Password, Rol);
                }else{
                    tvMessage.setTextColor(Color.RED);
                    tvMessage.setText("Debe ingresar todos los campos!");
                }

            }
        });
    }
}