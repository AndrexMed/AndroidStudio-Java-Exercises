package com.example.class10_mobiles_figurasgeometricas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CirculoActivity extends AppCompatActivity {

    TextView jtvarea,jtvperimetro;
    EditText jetRadio;
    String radio;
    float area,perimetro,radioCirculo;

    //Instanciando clases
    ClassAreas areas = new ClassAreas();
    ClassPerimetros perimetros = new ClassPerimetros();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circulo);

        //Asosciar Objetos de XML Con Java...
        jetRadio = findViewById(R.id.etRadio);
        jtvarea = findViewById(R.id.tvresultArea);
        jtvperimetro = findViewById(R.id.tvresultPerimetro);
    }

    public void Calcular(View view){
        radio = jetRadio.getText().toString();
        if (radio.isEmpty()){
            Toast.makeText(this, "Error!!", Toast.LENGTH_SHORT).show();
            jetRadio.requestFocus();
        }else{
            //Conversion de texto a decimal...
            radioCirculo = Float.parseFloat(radio);

            //Llamando objetos instanciados...
            area = areas.Calcular_areaCirculo(radioCirculo);
            perimetro = perimetros.Calcular_PerimetroCirculo(radioCirculo);
            //Mostrar resultados:
            jtvarea.setText(String.valueOf(area));
            jtvperimetro.setText(String.valueOf(perimetro));
        }
    }

    public void Limpiar(View view){
        jetRadio.setText("");
        jtvarea.setText("0");
        jtvperimetro.setText("0");
        jetRadio.requestFocus();
    }

    public void Regresar(View view){
        Intent intMain = new Intent(this, MainActivity.class);
        startActivity(intMain);
    }
}