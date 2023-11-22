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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class RentCar extends AppCompatActivity {

    private Handler handler;
    private Runnable updateTimeRunnable;

    Spinner spinner;
    TextView txtFechaHoraActual;
    EditText etFechaEntrega, etHoraEntrega;
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
    }//Fin Oncreate

    @Override //Para evitar fugas de memoria
    protected void onDestroy() {
        super.onDestroy();

        // Detener la actualización cuando la actividad se destruye para evitar pérdida de recursos
        handler.removeCallbacks(updateTimeRunnable);
    }

    private void setFechaActual() {
        // Obtén la fecha y hora actual
        Date currentDate = new Date();

        // Crea un formato de fecha y hora personalizado (puedes ajustar el formato según tus necesidades)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());

        // Convierte la fecha y hora actual al formato deseado
        String fechaHoraActual = dateFormat.format(currentDate);

        // Establece la fecha y hora actual en el TextView
        txtFechaHoraActual.setText(fechaHoraActual);
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
        // Actualizar el EditText con la fecha seleccionada
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
}