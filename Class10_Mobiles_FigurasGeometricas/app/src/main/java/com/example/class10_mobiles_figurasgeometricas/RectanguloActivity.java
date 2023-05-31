package com.example.class10_mobiles_figurasgeometricas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RectanguloActivity extends AppCompatActivity {

    TextView jtvarea,jtvperimetro;
    EditText jetbase,jetaltura;
    String base,altura;
    float area,perimetro,Base,Altura;


    //Instanciando clasess
    ClassAreas areas = new ClassAreas();
    ClassPerimetros perimetros = new ClassPerimetros();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rectangulo);

        //Asosciar Objetos de XML Con Java...
        jetbase = findViewById(R.id.etBase);
        jetaltura = findViewById(R.id.etAltura);
        jtvarea = findViewById(R.id.tvresultArea);
        jtvperimetro = findViewById(R.id.tvresultPerimetro);
    }

    public void Calcular(View view){
        base = jetbase.getText().toString();
        altura = jetaltura.getText().toString();
        if (base.isEmpty() || altura.isEmpty()){
            Toast.makeText(this, "Error!!", Toast.LENGTH_SHORT).show();
            jetbase.requestFocus();
        }else{
            //Conversion de texto a decimal...
            Base = Float.parseFloat(base);
            Altura = Float.parseFloat(altura);

            //Llamando objetos instanciados...
            area = areas.Calcular_areaRectangulo(Base, Altura);
            perimetro = perimetros.Calcular_PerimetroRectangulo(Base, Altura);
            //Mostrar resultados:
            jtvarea.setText(String.valueOf(area));
            jtvperimetro.setText(String.valueOf(perimetro));
        }
    }

    public void Limpiar(View view){
        jetbase.setText("");
        jetaltura.setText("");
        jtvarea.setText("0");
        jtvperimetro.setText("0");
        jetbase.requestFocus();
    }

    public void Regresar(View view){
        Intent intMain = new Intent(this, MainActivity.class);
        startActivity(intMain);
    }
}