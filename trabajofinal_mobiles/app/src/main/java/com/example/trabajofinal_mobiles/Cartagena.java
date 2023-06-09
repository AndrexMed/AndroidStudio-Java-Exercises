package com.example.trabajofinal_mobiles;

public class Cartagena {

    private int cantidad;
    private float precioPersona;
    private int Iva;
    private float Descuento;

    public Cartagena(int cantidad, float precioPersona) {
        this.cantidad = cantidad;
        this.precioPersona = precioPersona;
    }

    public void setIva(int iva) {
        Iva = iva;
    }

    public void setDescuento(float descuento) {
        Descuento = descuento;
    }

    public float totalBruto(){
        return this.cantidad * this.precioPersona;
    }

    public float totalIva(){
        return totalBruto() * Iva / 100;
    }

    public float totalDescuento(){
        return totalBruto() * Descuento / 100;
    }

    public float totalNeto(){
        return (totalBruto() - totalDescuento()) + totalIva();
    }
}
