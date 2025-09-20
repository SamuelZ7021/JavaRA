package com.minitienda.modelo;


public class Electrodomestico extends Product {

    public Electrodomestico(String nombre, double precio) {
        super(nombre, precio);
    }

    @Override
    public String getDescripcion() {
        return "Producto de uso en el hogar";
    }
}
