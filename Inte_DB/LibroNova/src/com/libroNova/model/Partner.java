package com.libroNova.model;

import java.time.LocalDateTime;

public class Partner {
    private int id;
    private String nombre;
    private String apellido;
    private String cc;
    private String email;
    private String passwordHash;
    private String  telefono;
    private String direccion;
    private LocalDateTime fechaRegistro;
    private String estado;

    public Partner(String nombre, String apellido, String cc, String email, String passwordHash, String telefono, String direccion, String estado) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cc = cc;
        this.email = email;
        this.passwordHash = passwordHash;
        this.telefono = telefono;
        this.direccion = direccion;
        this.estado = estado;
    }

    public Partner(int id, String nombre, String apellido, String cc, String email, String passwordHash, String telefono, String direccion, LocalDateTime fechaRegistro, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cc = cc;
        this.email = email;
        this.passwordHash = passwordHash;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String  getTelefono() {
        return telefono;
    }

    public void setTelefono(String  telefono) {
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
        return "=== SOCIO ===" + '\n' +
                "Id: " + id + '\n' +
                " Nombre: " + nombre + '\n' +
                " Apellido: " + apellido + '\n' +
                " CC: " + cc + '\n' +
                " Email: " + email + '\n' +
                " PasswordHash: " + passwordHash + '\n' +
                " Telefono: " + telefono + '\n' +
                " Direccion: " + direccion + '\n' +
                " FechaRegistro: " + fechaRegistro + '\n' +
                " Estado: " + estado + '\n' ;
    }
}
