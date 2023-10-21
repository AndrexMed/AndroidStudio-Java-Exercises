package com.example.apprentcars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RentCar extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText etPlate, etUserName;
    TextView tvMessage;
    Button btnBack, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_car);

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);

        tvMessage = findViewById(R.id.tvMessage);
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

                if (!plateNumber.isEmpty() || !userName.isEmpty()){
                    RentCar(plateNumber, userName);
                }else {
                    tvMessage.setTextColor(Color.RED);
                    tvMessage.setText("All fields are required");
                }
            }
        });

    }

    private void GoBack() {
        Intent exit = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(exit);
    }

    private void RentCar(String plateNumber, String userName) {
        db.collection("Users").whereEqualTo("userName", userName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        db.collection("Cars").whereEqualTo("plateNumber", plateNumber).whereEqualTo("carStatus", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().size() > 0) {
                                        task.getResult().getDocuments().get(0).getReference().update("carStatus", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(RentCar.this, "Status car are updated!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RentCar.this, "No se actualizo el estado del vehículo!!", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        Map<String, Object> dataUser = new HashMap<>();
                                        dataUser.put("plateNumber", plateNumber);
                                        dataUser.put("userName", userName);
                                        dataUser.put("dateRent", new Date().toString());

                                        db.collection("Rents")
                                                .add(dataUser)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                                        tvMessage.setTextColor(Color.GREEN);
                                                        tvMessage.setText("Car rent successfully");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        //Log.w(TAG, "Error adding document", e);
                                                        tvMessage.setTextColor(Color.RED);
                                                        tvMessage.setText("Error adding rent car");
                                                    }
                                                });

                                    } else {
                                        tvMessage.setTextColor(Color.RED);
                                        tvMessage.setText("Car not available");
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Internal error with car", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });

                    } else {
                        tvMessage.setTextColor(Color.RED);
                        tvMessage.setText("User not found");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Internal error with user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}