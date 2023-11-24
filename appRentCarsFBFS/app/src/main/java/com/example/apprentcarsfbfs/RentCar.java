package com.example.apprentcarsfbfs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class RentCar extends AppCompatActivity {

    private Handler handler;
    private Runnable updateTimeRunnable;

    Spinner spinner;
    TextView txtFechaHoraActual;
    EditText etFechaEntrega, etHoraEntrega;
    Button btRentCar;
    String userName;
    private Calendar calendar;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_car);

        spinner = findViewById(R.id.spinner);
        txtFechaHoraActual = findViewById(R.id.txtFechaHoraActual);
        etFechaEntrega = findViewById(R.id.etFechaEntrega);
        etHoraEntrega = findViewById(R.id.etHoraEntrega);
        btRentCar = findViewById(R.id.btRentCar);

        userName = getIntent().getStringExtra("userName");

        GetCarsAvailable();

        // Inicializar el Handler en el hilo principal (UI)
        handler = new Handler(Looper.getMainLooper());

        // Inicializar el Runnable para actualizar la hora
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                // Actualizar la hora actual
                setFechaActual();

                // Programar la próxima actualización después de 1 segundo
                handler.postDelayed(this, 1000);
            }
        };

        // Inicializar el calendario con la fecha actual
        calendar = Calendar.getInstance();

        etFechaEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        etHoraEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        // Iniciar la actualización de la hora
        handler.post(updateTimeRunnable);

        btRentCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rentCar();
            }
        });
    }//Fin Oncreate

    private void rentCar() {
        if(spinner.getSelectedItem() != null){
            String carSelected = spinner.getSelectedItem().toString();
            String dateInitial = setFechaActual();
            String dateReturnSelected = etFechaEntrega.getText().toString();
            String hourReturnSelected = etHoraEntrega.getText().toString();
            String dateConcat = dateReturnSelected +" "+ hourReturnSelected;

            if(!dateReturnSelected.isEmpty() && !hourReturnSelected.isEmpty()){
                saveRentInFirestore(carSelected, dateInitial, dateConcat, userName);
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No cars available", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveRentInFirestore(String carSelected, String dateInitial, String dateConcat, String userName) {

        long numberRent = System.currentTimeMillis();

        Map<String, Object> rentalData = new HashMap<>();
        rentalData.put("plateNumber", carSelected);
        rentalData.put("dateInitialRent", dateInitial);
        rentalData.put("dateFinalRent", dateConcat);
        rentalData.put("userName", userName);
        rentalData.put("status", true);
        rentalData.put("numberRent", numberRent);

        db.collection("Rentals")
                .add(rentalData)
                .addOnSuccessListener(documentReference -> {

                    Toast.makeText(this, "Car rented", Toast.LENGTH_SHORT).show();
                    updateCarStatus(db, carSelected, false);
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(this, "Internal error", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateCarStatus(FirebaseFirestore db, String plateNumber, boolean b) {
        db.collection("Cars")
                .whereEqualTo("PlateNumber", plateNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                        int selectedIndex = spinner.getSelectedItemPosition();
                        task.getResult().getDocuments().get(0).getReference().update("State", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(RentCar.this, "Status car are updated!!", Toast.LENGTH_SHORT).show();

                                // Elimina la placa del carro del Spinner
                                removePlateFromSpinner(selectedIndex);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RentCar.this, "No se actualizo el estado del vehículo!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                } else {
                    Toast.makeText(getApplicationContext(), "Internal error with car", Toast.LENGTH_SHORT).show();
                }
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

    private String setFechaActual() {
        // Obtén la fecha y hora actual
        Date currentDate = new Date();

        // Crea un formato de fecha y hora personalizado (puedes ajustar el formato según tus necesidades)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());

        // Convierte la fecha y hora actual al formato deseado
        String fechaHoraActual = dateFormat.format(currentDate);

        // Establece la fecha y hora actual en el TextView
        txtFechaHoraActual.setText(fechaHoraActual);

        return fechaHoraActual;
    }

    private void GetCarsAvailable(){
        db.collection("Cars").whereEqualTo("State", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    List<String> plateNumbers = new ArrayList<>();

                    for (QueryDocumentSnapshot document: task.getResult()){
                        plateNumbers.add(document.getString("PlateNumber"));
                    }

                    // Luego, utiliza el ArrayAdapter para cargar los datos en el Spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(RentCar.this, android.R.layout.simple_spinner_item, plateNumbers);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }else{
                    Toast.makeText(RentCar.this, "Internal error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Actualizar el calendario con la fecha seleccionada
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Verificar que la fecha seleccionada no sea menor que la fecha actual
                if (!isFechaEntregaValid()) {
                    showToast("La fecha de entrega no puede ser menor que la fecha actual");
                } else {
                    // Fecha válida, actualizar el EditText con la fecha seleccionada
                    updateFechaEntregaEditText();
                }
            }
        };

        // Mostrar el diálogo de selección de fecha
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Configurar para que la fecha mínima sea la fecha actual
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                // Actualizar el EditText con la hora seleccionada
                updateHoraEntregaEditText();
            }
        };

        new TimePickerDialog(
                this,
                timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        ).show();
    }

    private boolean isFechaEntregaValid() {
        // Obtener la fecha actual
        Date currentDate = new Date();

        // Obtener la fecha de entrega seleccionada
        Date fechaEntrega = calendar.getTime();

        // Comparar las fechas
        return !fechaEntrega.before(currentDate);
    }

    private void updateFechaEntregaEditText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        etFechaEntrega.setText(dateFormat.format(calendar.getTime()));
    }

    private void updateHoraEntregaEditText() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        etHoraEntrega.setText(timeFormat.format(calendar.getTime()));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override //Para evitar fugas de memoria
    protected void onDestroy() {
        super.onDestroy();

        // Detener la actualización cuando la actividad se destruye para evitar pérdida de recursos
        handler.removeCallbacks(updateTimeRunnable);
    }
}