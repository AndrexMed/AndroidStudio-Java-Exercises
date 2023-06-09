package com.example.trabajofinal_mobiles;

public class Servicios {
    private double cantidadAgua;
    private double valorAgua;
    private double cantidadEnergia;
    private double valorEnergia;
    private int estrato;

    public void setCantidadAgua(double cantidadAgua) {
        this.cantidadAgua = cantidadAgua;
    }

    public void setValorAgua(double valorAgua) {
        this.valorAgua = valorAgua;
    }

    public void setCantidadEnergia(double cantidadEnergia) {
        this.cantidadEnergia = cantidadEnergia;
    }

    public void setValorEnergia(double valorEnergia) {
        this.valorEnergia = valorEnergia;
    }

    public void setEstrato(int estrato) {
        this.estrato = estrato;
    }

    public double calcularTotalAgua(){
        return this.cantidadAgua * this.valorAgua;
    }

    public double calcularTotalEnergia(){
        return this.cantidadEnergia * this.valorEnergia;
    }

    public double calcularSubsidio(){
        int subsidio = 0;
        if (this.estrato <= 2){
            if (this.cantidadAgua <= 10){
                if (this.cantidadEnergia <= 100){
                    subsidio = 10;
                }
            }
        }
        return (calcularTotalAgua() + calcularTotalEnergia()) * subsidio / 100;
    }

    public double calcularNeto(){
        return calcularTotalAgua() + calcularTotalEnergia() - calcularSubsidio();
    }
}
