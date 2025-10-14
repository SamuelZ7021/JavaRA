package com.bliNva.service;

import com.bliNva.Interface.LibroDAO;
import com.bliNva.Interface.LibroServiceInterface;
import com.bliNva.dao.LibroDAOImpl;
import com.bliNva.model.Libro;

import java.util.List;
import java.util.Optional;

public class LibroService implements LibroServiceInterface {
    private final LibroDAO<Libro, Integer> libroDAO;

    public LibroService() {
        this.libroDAO = new LibroDAOImpl();
    }

    @Override
    public Libro crearLibro(Libro libro) {
        return libroDAO.create(libro);
    }

    @Override
    public List<Libro> obtenerTodosLosLibros() {
        return libroDAO.getAll();
    }

    @Override
    public Optional<Libro> buscarLibroPorId(Integer id) {
        return libroDAO.searchById(id);
    }

    @Override
    public Libro actualizarLibro(Libro libro) {
        return libroDAO.update(libro);
    }

    @Override
    public boolean eliminarLibro(Integer id) {
        return libroDAO.delete(id);
    }
}