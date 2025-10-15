package com.libroNova.service;

import com.libroNova.dao.BookDaoImpl;
import com.libroNova.interfas.BookDao;
import com.libroNova.interfas.BookServiceInterface;
import com.libroNova.model.Book;

import java.util.List;
import java.util.Optional;

public class BookService implements BookServiceInterface {
    private final BookDao<Book, Integer> bookDao;

    public BookService() {
        this.bookDao = new BookDaoImpl();
    }

    @Override
    public Book crearLibro(Book book) {
        return bookDao.create(book);
    }

    @Override
    public List<Book> obtenerTodosLosLibros() {
        return bookDao.getAll();
    }

    @Override
    public Optional<Book> buscarLibroPorId(Integer id) {
        return bookDao.searchById(id);
    }

    @Override
    public Book actualizarLibro(Book book) {
        return bookDao.update(book);
    }

    @Override
    public boolean eliminarLibro(Integer id) {
        return bookDao.delete(id);
    }
}
