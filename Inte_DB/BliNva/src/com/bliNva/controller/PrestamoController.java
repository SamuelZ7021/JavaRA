package com.bliNva.controller;

import com.bliNva.exception.BusinessException;
import com.bliNva.model.Prestamo;
import com.bliNva.service.PrestamoService;
import com.bliNva.service.PrestamoServiceInterface;
import com.bliNva.util.LoggerUtil;

import javax.swing.*;
import java.util.List;

public class PrestamoController {

    private final PrestamoServiceInterface prestamoService;

    public PrestamoController() {
        this.prestamoService = new PrestamoService();
    }

    public void gestionarPrestamos() {
        String opcion;
        do {
            opcion = JOptionPane.showInputDialog(
                    null,
                    "--- GESTIÓN DE PRÉSTAMOS ---\n" +
                            "1. Registrar Nuevo Préstamo\n" +
                            "2. Registrar Devolución\n" +
                            "3. Listar todos los Préstamos\n" +
                            "4. Volver al Menú Principal\n\n" +
                            "Elige una opción:",
                    "Menú de Préstamos",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion == null) {
                break;
            }

            switch (opcion) {
                case "1":
                    registrarNuevoPrestamo();
                    break;
                case "2":
                    registrarDevolucion();
                    break;
                case "3":
                    listarPrestamos();
                    break;
                case "4":
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.", "Error", JOptionPane.WARNING_MESSAGE);
                    break;
            }

        } while (opcion != null && !opcion.equals("4"));
    }

    private void registrarNuevoPrestamo() {
        try {
            String idLibroStr = JOptionPane.showInputDialog("Introduce el ID del Libro a prestar:");
            if (idLibroStr == null) return;
            int idLibro = Integer.parseInt(idLibroStr);

            String idSocioStr = JOptionPane.showInputDialog("Introduce el ID del Socio:");
            if (idSocioStr == null) return;
            int idSocio = Integer.parseInt(idSocioStr);

            String idUsuarioStr = JOptionPane.showInputDialog("Introduce el ID del Usuario que registra:");
            if (idUsuarioStr == null) return;
            int idUsuario = Integer.parseInt(idUsuarioStr);

            Prestamo nuevoPrestamo = prestamoService.registrarPrestamo(idLibro, idSocio, idUsuario);
            JOptionPane.showMessageDialog(null, "¡Préstamo registrado con éxito!\nID de Préstamo: " + nuevoPrestamo.getId(), "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Los IDs deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (BusinessException e) {
            LoggerUtil.logWarning("Error de negocio al registrar préstamo: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al registrar el préstamo:\n" + e.getMessage(), "Error de Negocio", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            LoggerUtil.logError("Error inesperado al registrar un préstamo.", e);
            JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarDevolucion() {
        try {
            int idPrestamo = Integer.parseInt(JOptionPane.showInputDialog("Introduce el ID del Préstamo a devolver:"));

            prestamoService.registrarDevolucion(idPrestamo);
            JOptionPane.showMessageDialog(null, "¡Devolución registrada con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID del préstamo debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar la devolución:\n" + e.getMessage(), "Error de Negocio", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarPrestamos() {
        List<Prestamo> prestamos = prestamoService.obtenerTodosLosPrestamos();
        if (prestamos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay préstamos registrados.");
        } else {
            StringBuilder sb = new StringBuilder("--- LISTADO DE PRÉSTAMOS ---\n\n");
            for (Prestamo p : prestamos) {
                sb.append("ID: ").append(p.getId())
                        .append(" | Libro ID: ").append(p.getIdLibro())
                        .append(" | Socio ID: ").append(p.getIdSocio())
                        .append(" | Estado: ").append(p.getEstado())
                        .append("\n");
            }
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(500, 300));
            JOptionPane.showMessageDialog(null, scrollPane, "Listado de Préstamos", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
