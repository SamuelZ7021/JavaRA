package com.libroNova.model;

public class Book {
    private int id;
    private String isbn;
    private String titulo;
    private String autor;
    private String editorial;
    private Integer anioPublicacion;
    private String genero;
    private int stockTotal;
    private int stockDisponible;


    public Book(String isbn, String titulo, String autor, String editorial, Integer anioPublicacion, String genero, int stockTotal, int stockDisponible) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.anioPublicacion = anioPublicacion;
        this.genero = genero;
        this.stockTotal = stockTotal;
        this.stockDisponible = stockDisponible;
    }


    public Book(int id, String isbn, String titulo, String autor, String editorial, Integer anioPublicacion, String genero, int stockTotal, int stockDisponible) {
        this(isbn, titulo, autor, editorial, anioPublicacion, genero, stockTotal, stockDisponible);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public Integer getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(Integer anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getStockTotal() {
        return stockTotal;
    }

    public void setStockTotal(int stockTotal) {
        this.stockTotal = stockTotal;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(int stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    @Override
    public String toString() {
        return "=== LIBROS DETALLADOS ===:\n" +
                "ID: " + id + "\n" +
                "ISBN: '" + isbn + "'\n" +
                "Título: '" + titulo + "'\n" +
                "Autor: '" + autor + "'\n" +
                "Editorial: '" + editorial + "'\n" +
                "Año: " + anioPublicacion + "\n" +
                "Género: '" + genero + "'\n" +
                "Stock Total: " + stockTotal + "\n" +
                "Stock Disponible: " + stockDisponible;
    }
}