package com.bliNva.service;

import com.bliNva.exception.BusinessException;
import com.bliNva.model.Prestamo;

import java.util.List;

public interface PrestamoServiceInterface {
    Prestamo registrarPrestamo(int idLibro, int idSocio, int idUsuario) throws BusinessException;
    Prestamo registrarDevolucion(int idPrestamo) throws BusinessException;
    List<Prestamo> obtenerTodosLosPrestamos();
    List<Prestamo> obtenerPrestamosVencidos(); // <-- MÉTODO NUEVO
}

