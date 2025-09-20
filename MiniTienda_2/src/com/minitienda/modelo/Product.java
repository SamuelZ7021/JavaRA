package com.minitienda.modelo;

import com.minitienda.Interface.Describible;


public abstract class Product implements Describible {
    private String nombre;
    private double precio;

    public Product(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public abstract String getDescripcion();
}
