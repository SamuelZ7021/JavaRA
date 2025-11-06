package com.libroNova.controller;


import com.libroNova.interfas.BookServiceInterface;
import com.libroNova.interfas.PrestamoServiceInterface;
import com.libroNova.model.Book;
import com.libroNova.model.Prestamo;
import com.libroNova.service.BookService;
import com.libroNova.service.PrestamoService;
import com.libroNova.util.CsvExportUtil;
import com.libroNova.util.LoggerUtil;
import com.libroNova.util.PropertiesLoader;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ReporterController {

    private final BookServiceInterface libroService;
    private final PrestamoServiceInterface prestamoService;

    public ReporterController() {
        this.libroService = new BookService();
        this.prestamoService = new PrestamoService();
    }

    public void gestionarReportes() {
        String opcion;
        do {
            opcion = JOptionPane.showInputDialog(
                    null,
                    "--- GESTIÓN DE REPORTES ---\n" +
                            "1. Exportar catálogo de libros a CSV\n" +
                            "2. Exportar préstamos a CSV\n" +
                            "3. Exportar prestamos vencidos\n" +
                            "4. Volver al Menú Principal\n\n" +
                            "Elige una opción:",
                    "Menú de Reportes",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion == null) break;

            switch (opcion) {
                case "1":
                    exportarCatalogoLibros();
                    break;
                case "2":
                    exportarPrestamos();
                    break;
                case "3":
                exportarPrestamosVencidos();
                break;
            }
        } while (opcion != null && !opcion.equals("4"));
    }

    private void exportarCatalogoLibros() {
        try {
            List<Book> books = libroService.obtenerTodosLosLibros();
            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay libros en el catálogo para exportar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String exportPath = PropertiesLoader.getProperty("csv.export.path");
            new File(exportPath).mkdirs();
            String filePath = exportPath + "catalogo_libros.csv";

            CsvExportUtil.exportLibrosToCsv(books, filePath);

            JOptionPane.showMessageDialog(null, "Catálogo de libros exportado con éxito en:\n" + filePath, "Exportación Completa", JOptionPane.INFORMATION_MESSAGE);
            LoggerUtil.logInfo("Catálogo de libros exportado a CSV. Total: " + books.size() + " libros.");

        } catch (IOException e) {
            LoggerUtil.logError("Error de E/S al exportar el catálogo de libros.", e);
            JOptionPane.showMessageDialog(null, "Ocurrió un error al escribir el archivo CSV.", "Error de Exportación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportarPrestamos() {
        try {
            List<Prestamo> prestamos = prestamoService.obtenerTodosLosPrestamos();
            if (prestamos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay préstamos para exportar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String exportPath = PropertiesLoader.getProperty("csv.export.path");
            new File(exportPath).mkdirs();
            String filePath = exportPath + "todos_los_prestamos.csv";

            CsvExportUtil.exportPrestamosToCsv(prestamos, filePath);

            JOptionPane.showMessageDialog(null, "Reporte de préstamos exportado con éxito en:\n" + filePath, "Exportación Completa", JOptionPane.INFORMATION_MESSAGE);
            LoggerUtil.logInfo("Préstamos exportados a CSV. Total: " + prestamos.size() + " préstamos.");

        } catch (IOException e) {
            LoggerUtil.logError("Error de E/S al exportar los préstamos.", e);
            JOptionPane.showMessageDialog(null, "Ocurrió un error al escribir el archivo CSV.", "Error de Exportación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportarPrestamosVencidos() {
        try {
            List<Prestamo> prestamos = prestamoService.obtenerPrestamosVencidos();
            if (prestamos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay préstamos para exportar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String exportPath = PropertiesLoader.getProperty("csv.export.path");
            new File(exportPath).mkdirs();
            String filePath = exportPath + "prestamos_vencidos.csv";

            CsvExportUtil.exportPrestamosToCsv(prestamos, filePath);

            JOptionPane.showMessageDialog(null, "Reporte de préstamos exportado con éxito en:\n" + filePath, "Exportación Completa", JOptionPane.INFORMATION_MESSAGE);
            LoggerUtil.logInfo("Préstamos exportados a CSV. Total: " + prestamos.size() + " préstamos.");

        } catch (IOException e) {
            LoggerUtil.logError("Error de E/S al exportar los préstamos.", e);
            JOptionPane.showMessageDialog(null, "Ocurrió un error al escribir el archivo CSV.", "Error de Exportación", JOptionPane.ERROR_MESSAGE);
        }
    }
}
