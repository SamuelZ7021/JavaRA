package com.libroNova.model;

import java.time.LocalDateTime;

public class Prestamo {

    private int id;
    private int idLibro;
    private int idSocio;
    private int idUsuarioPrestador;
    private LocalDateTime fechaPrestamo;
    private LocalDateTime fechaDevolucionEstimada;
    private LocalDateTime fechaDevolucionReal;
    private String estado;

    public Prestamo() {
    }

    public Prestamo(int idLibro, int idSocio, int idUsuarioPrestador, LocalDateTime fechaDevolucionEstimada, String estado) {
        this.idLibro = idLibro;
        this.idSocio = idSocio;
        this.idUsuarioPrestador = idUsuarioPrestador;
        this.fechaDevolucionEstimada = fechaDevolucionEstimada;
        this.estado = estado;
    }


    public Prestamo(int id, int idLibro, int idSocio, int idUsuarioPrestador, LocalDateTime fechaPrestamo, LocalDateTime fechaDevolucionEstimada, LocalDateTime fechaDevolucionReal, String estado) {
        this(idLibro, idSocio, idUsuarioPrestador, fechaDevolucionEstimada, estado);
        this.id = id;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionReal = fechaDevolucionReal;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public int getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(int idSocio) {
        this.idSocio = idSocio;
    }

    public int getIdUsuarioPrestador() {
        return idUsuarioPrestador;
    }

    public void setIdUsuarioPrestador(int idUsuarioPrestador) {
        this.idUsuarioPrestador = idUsuarioPrestador;
    }

    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDateTime fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDateTime getFechaDevolucionEstimada() {
        return fechaDevolucionEstimada;
    }

    public void setFechaDevolucionEstimada(LocalDateTime fechaDevolucionEstimada) {
        this.fechaDevolucionEstimada = fechaDevolucionEstimada;
    }

    public LocalDateTime getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public void setFechaDevolucionReal(LocalDateTime fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "=== PRESTAMO ===" + '\n' +
                "Id: " + id + '\n' +
                " IdLibro: " + idLibro + '\n' +
                " IdSocio: " + idSocio + '\n' +
                " IdUsuarioPrestador: " + idUsuarioPrestador + '\n' +
                " FechaPrestamo: " + fechaPrestamo + '\n' +
                " FechaDevolucionEstimada: " + fechaDevolucionEstimada + '\n' +
                " FechaDevolucionReal: " + fechaDevolucionReal + '\n' +
                " Estado: " + estado;
    }
}
