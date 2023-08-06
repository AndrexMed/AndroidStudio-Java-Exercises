package com.example.repasomobiles;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;
    int interes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencia al Spinner en el diseño
        spinner = findViewById(R.id.spinnerOne);

        // Datos para el Spinner
        String[] opciones = {"Educacion", "Vivienda", "Libre Inversion"};

        // Adaptador para el Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Asignar el adaptador al Spinner
        spinner.setAdapter(adapter);

        // Manejar la selección de elementos en el Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String opcionSeleccionada = parent.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, "Seleccionaste: " + opcionSeleccionada, Toast.LENGTH_SHORT).show();

                // Realizar acciones según la opción seleccionada
                switch (opcionSeleccionada) {
                    case "Educacion":
                        interes = 10;
                        showToastMessage(opcionSeleccionada);
                        break;
                    case "Vivienda":
                        interes = 5;
                        showToastMessage(opcionSeleccionada);
                        break;
                    case "Libre Inversion":
                        interes = 3;
                        showToastMessage(opcionSeleccionada);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se seleccionó nada
            }
            private void showToastMessage(String opcionSeleccionada) { //Mensaje Toast en una funcion
                Toast.makeText(MainActivity.this, "Seleccionaste: " + opcionSeleccionada, Toast.LENGTH_SHORT).show();
            }
        });


    }
}