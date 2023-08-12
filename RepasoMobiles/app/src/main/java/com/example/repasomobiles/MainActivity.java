package com.example.repasomobiles;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText valorPrestamo;
    private TextView totalDeuda;
    private TextView totalCuota;
    private Spinner spinner;
    private Spinner spinner2;
    double interes = 0;
    int numCuotas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencia al Spinner en el diseño
        spinner = findViewById(R.id.spinnerOne);
        spinner2 = findViewById(R.id.spinnerTwo);
        totalDeuda = findViewById(R.id.totalDeuda);
        totalCuota = findViewById(R.id.totalCuota);
        valorPrestamo = findViewById(R.id.valorPrestamo);


        // Datos para el Spinner
        String[] opciones = {"Educacion", "Vivienda", "Libre Inversion"};
        Integer[] numeroCuotas = {12,24,36};

        // Adaptador para el Spinner1
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adaptador para el Spinner2
        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numeroCuotas);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Asignar el adaptador al Spinner
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter2);

        // Manejar la selección de elementos en el Spinner1
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String opcionSeleccionada = parent.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, "Seleccionaste: " + opcionSeleccionada, Toast.LENGTH_SHORT).show();

                // Realizar acciones según la opción seleccionada
                switch (opcionSeleccionada) {
                    case "Educacion":
                        interes = 0.1;
                        showToastMessage(opcionSeleccionada);
                        break;
                    case "Vivienda":
                        interes = 0.2;
                        showToastMessage(opcionSeleccionada);
                        break;
                    case "Libre Inversion":
                        interes = 0.3;
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

        // Manejar la selección de elementos en el Spinner2
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String opcionSeleccionada = parent.getItemAtPosition(position).toString();
                switch (opcionSeleccionada) {
                    case "12":
                        numCuotas = 12;
                        break;
                    case "24":
                        numCuotas = 24;                        break;
                    case "36":
                        numCuotas = 36;                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void Calcular(View view){
        String valorPrestamoString = valorPrestamo.getText().toString();
        if (valorPrestamoString.isEmpty()){
            Toast.makeText(this, "Valor Prestamo Obligatorio", Toast.LENGTH_SHORT).show();
        }else{
            double valorPrestamo = Double.parseDouble(this.valorPrestamo.getText().toString());
            double totalDeudaNumber = valorPrestamo + ((valorPrestamo * this.interes) * this.numCuotas);
            double valorCuota = totalDeudaNumber / this.numCuotas;

            totalDeuda.setText(String.valueOf(totalDeudaNumber));
            totalCuota.setText(String.valueOf(valorCuota));
        }

    }
}