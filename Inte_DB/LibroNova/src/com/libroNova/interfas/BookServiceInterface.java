package com.libroNova.interfas;

import com.libroNova.model.Book;

import java.util.List;
import java.util.Optional;


public interface BookServiceInterface {
    Book crearLibro(Book book);
    List<Book> obtenerTodosLosLibros();
    Optional<Book> buscarLibroPorId(Integer id);
    Book actualizarLibro(Book book);
    boolean eliminarLibro(Integer id);
}
