package com.example.apprentcarsfbfs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Return_car extends AppCompatActivity {

    Button btReturnCar;

    Spinner spinner;
    String userName;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_car);

        spinner = findViewById(R.id.spinner);
        btReturnCar = findViewById(R.id.btReturnCar);

        userName = getIntent().getStringExtra("userName");

        GetCarsRented();

        btReturnCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnCar();
            }
        });

    }//Fin OnCreate

    private void returnCar() {
        if (spinner.getSelectedItem() != null){

        } else{
            Toast.makeText(this, "No cars rented!", Toast.LENGTH_SHORT).show();
        }
    }

    private void GetCarsRented(){
        db.collection("Rentals").whereEqualTo("userName", userName).whereEqualTo("status", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    List<String> plateNumbers = new ArrayList<>();

                    for (QueryDocumentSnapshot document: task.getResult()){
                        plateNumbers.add(document.getString("plateNumber"));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Return_car.this, android.R.layout.simple_spinner_item, plateNumbers);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }else{
                    Toast.makeText(Return_car.this, "Internal error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}