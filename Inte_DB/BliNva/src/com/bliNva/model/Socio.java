package com.bliNva.model;


import java.time.LocalDateTime;


public class Socio {

    private int id;
    private String nombre;
    private String apellido;
    private String cc;
    private String email;
    private String telefono;
    private String direccion;
    private LocalDateTime fechaRegistro;
    private String estado;


    public Socio(String nombre, String apellido, String cc, String email, String telefono, String direccion, String estado) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cc = cc;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.estado = estado;
    }


    public Socio(int id, String nombre, String apellido, String cc, String email, String telefono, String direccion, LocalDateTime fechaRegistro, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cc = cc;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaRegistro = fechaRegistro;
        this.estado = estado;
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Socio{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", cc='" + cc + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                ", estado='" + estado + '\'' +
                '}';
    }
}