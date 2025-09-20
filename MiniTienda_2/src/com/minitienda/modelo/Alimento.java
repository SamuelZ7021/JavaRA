package com.minitienda.modelo;


public class Alimento extends Product {

    public Alimento(String nombre, double precio) {
        super(nombre, precio);
    }

    @Override
    public String getDescripcion() {
        return "Producto de consumo";
    }
}
