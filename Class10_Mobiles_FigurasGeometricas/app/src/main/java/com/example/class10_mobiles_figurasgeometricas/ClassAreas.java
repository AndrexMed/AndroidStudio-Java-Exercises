package com.example.class10_mobiles_figurasgeometricas;

public class ClassAreas {

    float area;
    public float Calcular_areaCuadrado(float lado){
        area = lado * lado;
        return area;
    }

    public float Calcular_areaRectangulo(float base, float altura){
        area = base * altura;
        return area;
    }

    public float Calcular_areaCirculo(float radio){
        area = (float)Math.PI * radio * radio;
        return area;
    }
}
