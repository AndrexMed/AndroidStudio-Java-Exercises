package com.example.sobrecarga_exercise;

public class ClassMetodos {

    private int Descuento;
    private int Cantidad;
    private float ValorUnidad;
    private int IVA;

    float TotalBruto,TotalDescuento,TotalIva,TotalNeto;

    public ClassMetodos(int cantidad, float valorUnidad, int IVA, int Descuento) {
        this.Cantidad = cantidad;
        this.ValorUnidad = valorUnidad;
        this.IVA = IVA;
        this.Descuento = Descuento;
    }

    //TotalBruto
    public float Calculos(int Cantidad, float ValorUnidad){
        return this.Cantidad * this.ValorUnidad;
    }

    //Total Porcentaje IVA O  DESCUENTO
    public float Calculos(int Porcentaje){
        return (Cantidad*ValorUnidad) * Porcentaje / 100;
    }

    //TotalNeto
    public float Calculos(float IVA, float descuento){
        return (Cantidad*ValorUnidad) - descuento + IVA;
    }
}
