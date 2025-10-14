package com.bliNva.service;

import com.bliNva.model.Libro;

import java.util.List;
import java.util.Optional;


public interface LibroServiceInterface {
    Libro crearLibro(Libro libro);
    List<Libro> obtenerTodosLosLibros();
    Optional<Libro> buscarLibroPorId(Integer id);
    Libro actualizarLibro(Libro libro);
    boolean eliminarLibro(Integer id);
}
