package com.example.crud_usersandbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton btnRegister, btnBooks, btnRent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegister = findViewById(R.id.btnRegister);
        btnBooks = findViewById(R.id.btnBooks);
        btnRent = findViewById(R.id.btnRent);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });

        btnBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterBook();
            }
        });

        btnRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rent();
            }
        });
    }//Fin OnCreate

    private void Rent() {
        Intent rent = new Intent(this, RentBook.class);
        startActivity(rent);
    }

    public void Register(){
        Intent register = new Intent(this, Register.class);
        startActivity(register);
    }

    public void RegisterBook(){
        Intent newBook = new Intent(this, RegisterBook.class);
        startActivity(newBook);
    }

}