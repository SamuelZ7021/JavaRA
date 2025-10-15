package com.libroNova.model;

import java.time.LocalDateTime;

public class User {

    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String passwordHash;
    private int idRol;
    private LocalDateTime fechaCreacion;

    public User() {
    }

    public User(int id, String nombre, String apellido, String email, String passwordHash, int idRol, LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.passwordHash = passwordHash;
        this.idRol = idRol;
        this.fechaCreacion = fechaCreacion;
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

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "=== USUARIO === " + '\n' +
                "Id: " + id + '\n' +
                " Nombre: " + nombre + '\n' +
                " Apellido: " + apellido + '\n' +
                " Email: " + email + '\n' +
                " IdRol: " + idRol + '\n' +
                " FechaCreacion: " + fechaCreacion;
    }
}

