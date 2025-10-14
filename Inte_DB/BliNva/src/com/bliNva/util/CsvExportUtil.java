package com.bliNva.util;

import com.bliNva.model.Libro;
import com.bliNva.model.Prestamo;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class CsvExportUtil {

    public static void exportLibrosToCsv(List<Libro> libros, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {

            writer.println("ID,ISBN,Titulo,Autor,Editorial,Anio,Genero,StockTotal,StockDisponible");

            for (Libro libro : libros) {
                writer.printf("%d,\"%s\",\"%s\",\"%s\",\"%s\",%s,\"%s\",%d,%d\n",
                        libro.getId(),
                        libro.getIsbn(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getEditorial(),
                        libro.getAnioPublicacion() != null ? libro.getAnioPublicacion().toString() : "",
                        libro.getGenero(),
                        libro.getStockTotal(),
                        libro.getStockDisponible());
            }
        }
    }


    public static void exportPrestamosToCsv(List<Prestamo> prestamos, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {

            writer.println("ID_Prestamo,ID_Libro,ID_Socio,Fecha_Prestamo,Fecha_Devolucion_Estimada,Estado");

            for (Prestamo prestamo : prestamos) {
                writer.printf("%d,%d,%d,%s,%s,%s\n",
                        prestamo.getId(),
                        prestamo.getIdLibro(),
                        prestamo.getIdSocio(),
                        prestamo.getFechaPrestamo().toString(),
                        prestamo.getFechaDevolucionEstimada().toString(),
                        prestamo.getEstado());
            }
        }
    }
}
