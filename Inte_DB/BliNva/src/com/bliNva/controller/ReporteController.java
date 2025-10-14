package com.bliNva.controller;

import com.bliNva.model.Libro;
import com.bliNva.model.Prestamo;
import com.bliNva.service.LibroService;
import com.bliNva.service.LibroServiceInterface;
import com.bliNva.service.PrestamoService;
import com.bliNva.service.PrestamoServiceInterface;
import com.bliNva.util.CsvExportUtil;
import com.bliNva.util.LoggerUtil;
import com.bliNva.util.PropertiesLoader;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ReporteController {

    private final LibroServiceInterface libroService;
    private final PrestamoServiceInterface prestamoService;

    public ReporteController() {
        this.libroService = new LibroService();
        this.prestamoService = new PrestamoService();
    }

    public void gestionarReportes() {
        String opcion;
        do {
            opcion = JOptionPane.showInputDialog(
                    null,
                    "--- GESTIÓN DE REPORTES ---\n" +
                            "1. Exportar catálogo de libros a CSV\n" +
                            "2. Exportar préstamos vencidos a CSV\n" +
                            "3. Volver al Menú Principal\n\n" +
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
                    exportarPrestamosVencidos();
                    break;
            }
        } while (opcion != null && !opcion.equals("3"));
    }

    private void exportarCatalogoLibros() {
        try {
            List<Libro> libros = libroService.obtenerTodosLosLibros();
            if (libros.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay libros en el catálogo para exportar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String exportPath = PropertiesLoader.getProperty("csv.export.path");
            new File(exportPath).mkdirs();
            String filePath = exportPath + "catalogo_libros.csv";

            CsvExportUtil.exportLibrosToCsv(libros, filePath);

            JOptionPane.showMessageDialog(null, "Catálogo de libros exportado con éxito en:\n" + filePath, "Exportación Completa", JOptionPane.INFORMATION_MESSAGE);
            LoggerUtil.logInfo("Catálogo de libros exportado a CSV. Total: " + libros.size() + " libros.");

        } catch (IOException e) {
            LoggerUtil.logError("Error de E/S al exportar el catálogo de libros.", e);
            JOptionPane.showMessageDialog(null, "Ocurrió un error al escribir el archivo CSV.", "Error de Exportación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportarPrestamosVencidos() {
        try {
            List<Prestamo> prestamos = prestamoService.obtenerPrestamosVencidos();
            if (prestamos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay préstamos vencidos para exportar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String exportPath = PropertiesLoader.getProperty("csv.export.path");
            new File(exportPath).mkdirs();
            String filePath = exportPath + "prestamos_vencidos.csv";

            CsvExportUtil.exportPrestamosToCsv(prestamos, filePath);

            JOptionPane.showMessageDialog(null, "Reporte de préstamos vencidos exportado con éxito en:\n" + filePath, "Exportación Completa", JOptionPane.INFORMATION_MESSAGE);
            LoggerUtil.logInfo("Préstamos vencidos exportados a CSV. Total: " + prestamos.size() + " préstamos.");

        } catch (IOException e) {
            LoggerUtil.logError("Error de E/S al exportar los préstamos vencidos.", e);
            JOptionPane.showMessageDialog(null, "Ocurrió un error al escribir el archivo CSV.", "Error de Exportación", JOptionPane.ERROR_MESSAGE);
        }
    }
}
