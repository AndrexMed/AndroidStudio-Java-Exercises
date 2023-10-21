package com.example.apprentcars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RentCar extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText etPlate, etUserName;
    TextView tvtitlerentacar;
    Button btnBack, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_car);

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);

        tvtitlerentacar = findViewById(R.id.tvtitlerentacar);
        etPlate = findViewById(R.id.etPlate);
        etUserName = findViewById(R.id.etUserName);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoBack();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String plateNumber = etPlate.getText().toString();
                String userName = etUserName.getText().toString();

                RentCar(plateNumber, userName);
            }
        });

    }

    private void GoBack() {
        Intent exit = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(exit);
    }

    private void RentCar(String plateNumber, String userName){
        // Verificar disponibilidad del carro
        db.collection("Cars").document(plateNumber).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        boolean carStatus = document.getBoolean("carStatus");
                        if (carStatus) {
                            // Verificar la existencia del usuario
                            db.collection("Users").document(userName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot userDocument = task.getResult();
                                        if (userDocument.exists()) {
                                            // Realizar el alquiler, actualizando la disponibilidad del carro y asignando al usuario
                                            db.collection("Cars").document(plateNumber).update("carStatus", false, "usuario", userName);
                                            Toast.makeText(RentCar.this, "Alquiler realizado con éxito.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(RentCar.this, "El usuario no existe.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(RentCar.this, "Error al verificar el usuario.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RentCar.this, "El carro no está disponible.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RentCar.this, "La placa no existe.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RentCar.this, "Error al verificar el carro.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}