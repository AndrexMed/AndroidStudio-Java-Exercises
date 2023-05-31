package com.example.class10_mobiles_figurasgeometricas;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CuadradoActivity extends AppCompatActivity {

    TextView jtvarea,jtvperimetro;
    EditText jetlado;
    String lado;
    float area,perimetro,ladoCuadrado;

    //Instanciando clases
    ClassAreas areas = new ClassAreas();
    ClassPerimetros perimetros = new ClassPerimetros();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuadrado);

        //Ocultar barra de titulo...
        // getSupportActionBar().hide();

        //Asosciar Objetos de XML Con Java...
            jetlado = findViewById(R.id.etCuadrado);
            jtvarea = findViewById(R.id.tvresultArea);
            jtvperimetro = findViewById(R.id.tvresultPerimetro);
    }

    public void Calcular(View view){
        lado = jetlado.getText().toString();
        if (lado.isEmpty()){
            Toast.makeText(this, "Error!!", Toast.LENGTH_SHORT).show();
            jetlado.requestFocus();
        }else{
            //Conversion de texto a decimal...
            ladoCuadrado = Float.parseFloat(lado);

            //Llamando objetos instanciados...
            area = areas.Calcular_areaCuadrado(ladoCuadrado);
            perimetro = perimetros.calcular_PerimetroCuadrado(ladoCuadrado);
            //Mostrar resultados:
            jtvarea.setText(String.valueOf(area));
            jtvperimetro.setText(String.valueOf(perimetro));
        }
    }

    public void Limpiar(View view){
    jetlado.setText("");
    jtvarea.setText("0");
    jtvperimetro.setText("0");
    jetlado.requestFocus();
    }

    public void Regresar(View view){
        Intent intMain = new Intent(this, MainActivity.class);
        startActivity(intMain);
    }
}