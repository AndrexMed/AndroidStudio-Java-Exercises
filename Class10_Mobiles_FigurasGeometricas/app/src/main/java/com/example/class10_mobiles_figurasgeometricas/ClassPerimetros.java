package com.example.class10_mobiles_figurasgeometricas;

public class ClassPerimetros {

    float perimetro;

    public float calcular_PerimetroCuadrado(float lado){
        perimetro = lado * 4;
        return  perimetro;
    }

    public float Calcular_PerimetroRectangulo(float base, float altura){
        perimetro = 2 * (base + altura);
        return perimetro;
    }

    public float Calcular_PerimetroCirculo(float radio){
        perimetro = (float)Math.PI * radio * 2;
        return perimetro;
    }
}
