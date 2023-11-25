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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private void returnCar() {
        if (spinner.getSelectedItem() != null){

            String carSelected = spinner.getSelectedItem().toString();
            int selectedIndex = spinner.getSelectedItemPosition();

            db.collection("Rentals")
                    .whereEqualTo("userName", userName)
                    .whereEqualTo("plateNumber", carSelected)
                    .whereEqualTo("status", true)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()){

                                QueryDocumentSnapshot rentalDocument = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                                long rentalNumber = rentalDocument.getLong("numberRent");

                                Toast.makeText(Return_car.this,String.valueOf(rentalNumber), Toast.LENGTH_SHORT).show();

                                UpdateRentalInFS(task, selectedIndex);
                                insertIntoCarsReturned(rentalNumber);

                            } else{
                                Toast.makeText(Return_car.this, "Internal Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else{
            Toast.makeText(this, "No cars rented!", Toast.LENGTH_SHORT).show();
        }
    }

    private void UpdateRentalInFS(Task<QuerySnapshot> task, int selectedIndex) {
        task.getResult().getDocuments().get(0).getReference().update("status", false).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Return_car.this, "Rental disabled!!", Toast.LENGTH_SHORT).show();

                // Elimina la placa del carro del Spinner
                removePlateFromSpinner(selectedIndex);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Return_car.this, "Update rentals failed!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertIntoCarsReturned(long rentNumber) {
        // Obtener la fecha actual en el formato deseado
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateFormat.format(new Date());

        long numberRent = System.currentTimeMillis();

        // Crear un nuevo mapa con los datos a insertar
        Map<String, Object> carReturnedData = new HashMap<>();
        carReturnedData.put("returnNumber", numberRent);
        carReturnedData.put("numberRent", rentNumber);
        carReturnedData.put("returnDate", currentDate);

        // Insertar el nuevo registro en la colección "CarsReturned"
        db.collection("CarsReturned")
                .add(carReturnedData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Return_car.this, "Car return record added successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Return_car.this, "Error adding car return record!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removePlateFromSpinner(int selectedIndex) {
        // Obtén el adaptador del Spinner
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();

        // Elimina la placa del carro del adaptador
        adapter.remove(adapter.getItem(selectedIndex));

        // Notifica al adaptador que los datos han cambiado
        adapter.notifyDataSetChanged();
    }
}