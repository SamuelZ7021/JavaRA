package com.universidad.model;

public abstract class Persona {
    private int id;
    protected String nombre;
    protected int edad;

    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    @Override
    public String toString() {
        return "Persona" +
                "id: " + id + '\n' +
                ", nombre: " + nombre + '\n' +
                ", edad: " + edad
                ;
    }
}
