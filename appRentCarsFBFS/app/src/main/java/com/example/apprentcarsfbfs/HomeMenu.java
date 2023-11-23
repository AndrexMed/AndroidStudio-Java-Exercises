package com.example.apprentcarsfbfs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeMenu extends AppCompatActivity {

    TextView tvTitleUserName;
    Button btnRegisterCar, btnCarsRented, btnRentCar, btnCarsAvailables;
    LinearLayout AdminMenu;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

        tvTitleUserName = findViewById(R.id.tvTitleUserName);

        btnRentCar = findViewById(R.id.btnRentCar);
        btnCarsAvailables = findViewById(R.id.btnCarsAvailables);

        AdminMenu = findViewById(R.id.AdminMenu);
        btnRegisterCar = findViewById(R.id.btnRegisterCar);
        btnCarsRented = findViewById(R.id.btnCarsRented);

        userName = getIntent().getStringExtra("userName");
        tvTitleUserName.setText("Welcome "+userName);

        String role = getIntent().getStringExtra("role");

        if (role.equals("Admin")) {
            btnRegisterCar.setEnabled(true);
            btnCarsRented.setEnabled(true);
            AdminMenu.setVisibility(View.VISIBLE);
        }

        btnRegisterCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoRegisterCar();
            }
        });

        btnCarsRented.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoCarsRented();
            }
        });

        btnRentCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoRentCar();
            }
        });

        btnCarsAvailables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoCarsAvailables();
            }
        });
    }

    private void GoCarsAvailables() {
        Intent carAvailable = new Intent(getApplicationContext(), CarsAvailable.class);
        startActivity(carAvailable);
    }

    private void GoRentCar() {
        Intent rentCar = new Intent(getApplicationContext(), RentCar.class);
        rentCar.putExtra("userName", userName);
        startActivity(rentCar);
    }

    private void GoRegisterCar() {
        Intent registerCar = new Intent(getApplicationContext(), CarRegister.class);
        startActivity(registerCar);
    }

    private void GoCarsRented() {
        Intent carsRented = new Intent(getApplicationContext(), CarsRented.class);
        startActivity(carsRented);
    }
}