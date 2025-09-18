package com.universidad.model;

public abstract class Persona {
    private static int contadorId = 1;
    private final int id;
    protected String nombre;
    protected int edad;

    public Persona(String nombre, int edad) {
        this.id = contadorId++;
        this.nombre = nombre;
        this.edad = edad;
    }

    public int getId() {
        return id;
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
                "id:" + id +
                ", nombre:'" + nombre + '\'' +
                ", edad:" + edad +
                '}';
    }

    public abstract void mostrarInformacion();
}
