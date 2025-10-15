package com.libroNova.interfas;

import com.libroNova.exception.BusinessException;
import com.libroNova.model.Prestamo;

import java.util.List;

public interface PrestamoServiceInterface {
    Prestamo registrarPrestamo(int idLibro, int idSocio, int idUsuario) throws BusinessException;
    Prestamo registrarDevolucion(int idPrestamo) throws BusinessException;
    List<Prestamo> obtenerTodosLosPrestamos();
    List<Prestamo> obtenerPrestamosVencidos();
}