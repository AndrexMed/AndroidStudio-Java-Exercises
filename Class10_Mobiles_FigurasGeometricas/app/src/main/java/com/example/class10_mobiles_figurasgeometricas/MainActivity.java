package com.example.class10_mobiles_figurasgeometricas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }// Fin onCreate

    public void Cuadrado(View view){
      Intent intRectangulo=new Intent(this,CuadradoActivity.class);
      startActivity(intRectangulo);
    }

    public void Rectangulo(View view){
        Intent intRectangulo = new Intent(this, RectanguloActivity.class);
        startActivity(intRectangulo);
    }

    public void Circulo(View view){
        Intent intCirculo = new Intent(this, CirculoActivity.class);
        startActivity(intCirculo);
}
}