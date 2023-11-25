package com.example.apprentcarsfbfs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CarRegister extends AppCompatActivity {

    EditText etPlateNumber, etBrand, etValue;
    CheckBox btnState;

    Button btCreate, btRead, btUpdate, btDelete;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String idAutomatic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_register);

        etPlateNumber = findViewById(R.id.etPlateNumber);
        etBrand = findViewById(R.id.etBrand);
        etValue = findViewById(R.id.etValue);
        btnState = findViewById(R.id.btnState);

        btCreate = findViewById(R.id.btCreate);
        btRead = findViewById(R.id.btRead);
        btUpdate = findViewById(R.id.btUpdate);
        btDelete = findViewById(R.id.btDelete);

        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerCar();
            }
        });

        btRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String plateNumber = etPlateNumber.getText().toString();
                if (!plateNumber.isEmpty()){
                    searchCar(plateNumber);
                } else{
                    Toast.makeText(CarRegister.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              updateCar();
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String plateNumberToDelete = etPlateNumber.getText().toString();
                
                if(!plateNumberToDelete.isEmpty()){
                    deleteUserInFS(plateNumberToDelete);
                } else {
                    Toast.makeText(CarRegister.this, "PlateNumber is required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteUserInFS(String plateNumberToDelete) {
        db.collection("Cars")
                .whereEqualTo("PlateNumber", plateNumberToDelete)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String idAutomatic = document.getId();
                                    db.collection("Cars").document(idAutomatic).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(CarRegister.this, "Car deleted!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(CarRegister.this, "Car not found to eliminated", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CarRegister.this, "Internal error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateCar() {

        String plateNumberToUpdate = etPlateNumber.getText().toString();
        String brandToUpdate = etBrand.getText().toString();
        String dailyValueToUpdate = etValue.getText().toString();
        boolean stateToUpdate = btnState.isChecked();

        if(!plateNumberToUpdate.isEmpty()){
            updateCarInFS(plateNumberToUpdate, brandToUpdate, dailyValueToUpdate, stateToUpdate);
        } else {
            Toast.makeText(CarRegister.this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCarInFS(String plateNumberToUpdate, String brandToUpdate, String dailyValueToUpdate, boolean stateToUpdate) {
        db.collection("Cars")
                .whereEqualTo("PlateNumber", plateNumberToUpdate)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String idAutomatic = document.getId();
                                    Map<String, Object> dataUser = new HashMap<>();
                                    dataUser.put("PlateNumber", plateNumberToUpdate);
                                    dataUser.put("Brand", brandToUpdate);
                                    dataUser.put("State", stateToUpdate);
                                    dataUser.put("DailyValue", dailyValueToUpdate);

                                    db.collection("Cars").document(idAutomatic).set(dataUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(CarRegister.this, "Car Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(CarRegister.this, "Car not found to updated", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CarRegister.this, "Internal Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void searchCar(String plateNumber) {
        db.collection("Cars").whereEqualTo("PlateNumber", plateNumber).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            idAutomatic = document.getId();
                            etBrand.setText(document.getString("Brand"));
                            etValue.setText(document.getString("DailyValue"));
                            btnState.setChecked(document.getBoolean("State"));
                        }
                    } else {
                        Toast.makeText(CarRegister.this, "Car not found", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CarRegister.this, "Internal Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerCar() {
        String plateNumber = etPlateNumber.getText().toString();
        String brand = etBrand.getText().toString();
        String dailyValue = etValue.getText().toString();
        boolean state = btnState.isChecked();

        if(!plateNumber.isEmpty() && !brand.isEmpty() && !dailyValue.isEmpty()){
            saveCarInFS(plateNumber, brand, dailyValue, state);
        } else{
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveCarInFS(String plateNumber, String brand, String dailyValue, boolean carStatus) {
        db.collection("Cars").whereEqualTo("PlateNumber", plateNumber).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    Map<String, Object> dataUser = new HashMap<>();
                    dataUser.put("PlateNumber", plateNumber);
                    dataUser.put("Brand", brand);
                    dataUser.put("State", carStatus);
                    dataUser.put("DailyValue", dailyValue);
                    db.collection("Cars").add(dataUser).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(CarRegister.this, "Car register is successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CarRegister.this, "Error to create car", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(CarRegister.this, "Car already exist", Toast.LENGTH_SHORT).show();
                }
                //} else {
                //  showErrorMessage("All fields are required");
                //}
            }
        });
    }

    private void clearData(){

    }
}