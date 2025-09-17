package com.universidad.model;

import javax.swing.*;

public class Persona {
    protected static String nombre;
    protected int edad;
    protected char sexo;
    protected String direccion;
    protected int telefono;
    protected String email;

    public Persona(String nombre, int edad, char sexo, String direccion, int telefono, String email) {
        this.nombre = nombre;
        this.edad = edad;
        this.sexo = sexo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "nombre:'" + nombre + '\'' +
                ", edad:" + edad +
                ", sexo:" + sexo +
                ", direccion='" + direccion + '\'' +
                ", telefono=" + telefono +
                ", email='" + email + '\'' +
                '}';
    }

    public void mostrarInformacion(){
        JOptionPane.showMessageDialog(null, "Nombre del Alumno" + nombre + "Edad" + edad + "Sexo" + sexo + "Dirección" + direccion + "Telefono" + telefono + "Email" + email);
    }
}
