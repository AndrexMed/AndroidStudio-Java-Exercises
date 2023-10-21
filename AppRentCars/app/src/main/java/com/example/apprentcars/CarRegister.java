package com.example.apprentcars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CarRegister extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText etplateNumber, etbrand, etstate;
    CheckBox btStatus;
    Button btregisterCar, btback;
    TextView tvregisterCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_register);

        etplateNumber = findViewById(R.id.etplateNumber);
        etbrand = findViewById(R.id.etbrand);
        etstate = findViewById(R.id.etstate);
        btStatus = findViewById(R.id.btStatus);

        btregisterCar = findViewById(R.id.btregisterCar);
        btback = findViewById(R.id.btback);

        tvregisterCar = findViewById(R.id.tvregisterCar);

        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoBack();
            }
        });

        btregisterCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String plateNumber = etplateNumber.getText().toString();
                String brand = etbrand.getText().toString();
                String state = etstate.getText().toString();
                boolean carStatus = btStatus.isChecked();

                boolean isDataCarValid = IsDataCarValid(plateNumber, brand, state);

                if (isDataCarValid) {
                    CarRegister(plateNumber, brand, state, carStatus);
                } else {
                    tvregisterCar.setTextColor(Color.RED);
                    tvregisterCar.setText("All fields are required");
                }
            }
        });
    }

    private void GoBack() {
        Intent GoMain = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(GoMain);
    }

    private void CarRegister(String plateNumber, String brand, String state, boolean carStatus) {
        db.collection("Cars").whereEqualTo("plateNumber", plateNumber).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    Map<String, Object> dataUser = new HashMap<>();
                    dataUser.put("plateNumber", plateNumber);
                    dataUser.put("brand", brand);
                    dataUser.put("state", state);
                    dataUser.put("carStatus", carStatus);
                    db.collection("Cars").add(dataUser).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            tvregisterCar.setTextColor(Color.GREEN);
                            tvregisterCar.setText("Car registered");
                            clearData();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showErrorMessage("Error 500");
                        }
                    });
                } else {
                    showErrorMessage("Car already exist!");
                }
                //} else {
                //  showErrorMessage("All fields are required");
                //}
            }
        });
    }

    private boolean IsDataCarValid(String plateNumber, String brand, String state) {
        if (plateNumber.isEmpty() || brand.isEmpty() || state.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private void clearData() {
        etplateNumber.setText("");
        etbrand.setText("");
        etstate.setText("");
    }

    private void showErrorMessage(String message) {
        tvregisterCar.setTextColor(Color.RED);
        tvregisterCar.setText(message);
        clearData();
    }
}