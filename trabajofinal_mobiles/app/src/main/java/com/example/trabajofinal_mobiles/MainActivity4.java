package com.example.trabajofinal_mobiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity4 extends AppCompatActivity {

    EditText etCantidadAgua, etValorAgua, etCantidadEnergia, etValorEnergia, etEstrato;

    TextView tvTotalAgua, tvTotalEnergia, tvTotalSubsidio, tvTotalNeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        etCantidadAgua = findViewById(R.id.etCantidadAgua);
        etValorAgua = findViewById(R.id.etValorAgua);
        etCantidadEnergia = findViewById(R.id.etCantidadEnergia);
        etValorEnergia = findViewById(R.id.etValorEnergia);
        etEstrato = findViewById(R.id.etEstrato);

        tvTotalAgua = findViewById(R.id.tvTotalAgua);
        tvTotalEnergia = findViewById(R.id.tvTotalEnergia);
        tvTotalSubsidio = findViewById(R.id.tvTotalSubsidio);
        tvTotalNeto = findViewById(R.id.tvTotalNeto);
    }

    public void CalcularServicios(View view) {
        String cantidadAguaString = etCantidadAgua.getText().toString();
        String valorAguaString = etValorAgua.getText().toString();
        String cantidadEnergiaString = etCantidadEnergia.getText().toString();
        String valorEnergiaString = etValorEnergia.getText().toString();
        String estratoString = etEstrato.getText().toString();

        if (!cantidadAguaString.isEmpty() &&
                !valorAguaString.isEmpty() &&
                !cantidadEnergiaString.isEmpty() &&
                !valorEnergiaString.isEmpty() &&
                !estratoString.isEmpty()) {

            double cantidadAgua = Double.parseDouble(cantidadAguaString);
            double valorAgua = Double.parseDouble(valorAguaString);
            double cantidadEnergia = Double.parseDouble(cantidadEnergiaString);
            double valorEnergia = Double.parseDouble(valorEnergiaString);
            int estrato = Integer.parseInt(estratoString);

            Servicios servicios = new Servicios();
            servicios.setCantidadAgua(cantidadAgua);
            servicios.setValorAgua(valorAgua);
            servicios.setCantidadEnergia(cantidadEnergia);
            servicios.setValorEnergia(valorEnergia);
            servicios.setEstrato(estrato);

            double TotalAgua = servicios.calcularTotalAgua();
            tvTotalAgua.setText(String.valueOf(TotalAgua));

            double TotalEnergia = servicios.calcularTotalEnergia();
            tvTotalEnergia.setText(String.valueOf(TotalEnergia));

            double TotalSubsidio = servicios.calcularSubsidio();
            tvTotalSubsidio.setText(String.valueOf(TotalSubsidio));

            double TotalNeto = servicios.calcularNeto();
            tvTotalNeto.setText(String.valueOf(TotalNeto));

        }else{
            Toast.makeText(this, "Campos obligatorios!", Toast.LENGTH_SHORT).show();
        }
    }

    public void LimpiarServicios(View view){
        etCantidadAgua.setText("");
        etValorAgua.setText("");
        etCantidadEnergia.setText("");
        etValorEnergia.setText("");
        etEstrato.setText("");
        tvTotalAgua.setText("");
        tvTotalEnergia.setText("");
        tvTotalSubsidio.setText("");
        tvTotalNeto.setText("");
        etCantidadAgua.requestFocus();
        Toast.makeText(this, "Se Limpio Correctamente!", Toast.LENGTH_SHORT).show();
    }

    public void RegresarServicios(View view){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
}