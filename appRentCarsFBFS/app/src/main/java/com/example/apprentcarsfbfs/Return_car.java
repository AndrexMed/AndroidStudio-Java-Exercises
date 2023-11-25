package com.example.apprentcarsfbfs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Return_car extends AppCompatActivity {

    TextView tvFechaSistema;

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
        tvFechaSistema = findViewById(R.id.tvFechaSistema);

        userName = getIntent().getStringExtra("userName");

        GetCarsRented();
        tvFechaSistema.setText(setFechaActual());

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

    private String setFechaActual() {
        // Obtén la fecha y hora actual
        Date currentDate = new Date();

        // Crea un formato de fecha y hora personalizado (puedes ajustar el formato según tus necesidades)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());

        // Convierte la fecha y hora actual al formato deseado
        String fechaHoraActual = dateFormat.format(currentDate);

        // Establece la fecha y hora actual en el TextView
        tvFechaSistema.setText(fechaHoraActual);

        return fechaHoraActual;
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
                                String plateNumber = rentalDocument.getString("plateNumber");

                                UpdateRentalInFS(task, selectedIndex);
                                insertIntoCarsReturned(rentalNumber);
                                changeStateCarToAvailable(plateNumber);

                                Toast.makeText(Return_car.this, "Successful car delivery", Toast.LENGTH_SHORT).show();

                            } else{
                                Toast.makeText(Return_car.this, "Internal Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else{
            Toast.makeText(this, "No cars rented!", Toast.LENGTH_SHORT).show();
        }
    }

    private void changeStateCarToAvailable(String plateNumber) {
        db.collection("Cars")
                .whereEqualTo("PlateNumber", plateNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            task.getResult().getDocuments().get(0).getReference().update("State", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //Toast.makeText(Return_car.this, "Status car are updated!!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Return_car.this, "No se actualizo el estado del vehículo!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Internal error with car", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void UpdateRentalInFS(Task<QuerySnapshot> task, int selectedIndex) {
        task.getResult().getDocuments().get(0).getReference().update("status", false).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //Toast.makeText(Return_car.this, "Rental disabled!!", Toast.LENGTH_SHORT).show();

                // Elimina la placa del carro del Spinner
                removePlateFromSpinner(selectedIndex);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Return_car.this, "Update status rentals failed!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertIntoCarsReturned(long rentNumber) {

        String returnDate = setFechaActual();

        long numberRent = System.currentTimeMillis();

        // Crear un nuevo mapa con los datos a insertar
        Map<String, Object> carReturnedData = new HashMap<>();
        carReturnedData.put("returnNumber", numberRent);
        carReturnedData.put("numberRent", rentNumber);
        carReturnedData.put("returnDate", returnDate);

        // Insertar el nuevo registro en la colección "CarsReturned"
        db.collection("CarsReturned")
                .add(carReturnedData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Toast.makeText(Return_car.this, "Car return record added successfully!", Toast.LENGTH_SHORT).show();
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