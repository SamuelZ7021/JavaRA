package com.bliNva.controller;

import com.bliNva.model.Libro;
import com.bliNva.service.LibroService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;


public class LibroController {

    private final LibroService libroService;

    public LibroController() {
        this.libroService = new LibroService();
    }

    public void gestionarLibros() {
        String opcion;
        do {
            opcion = JOptionPane.showInputDialog(
                    null,
                    "--- MENÚ GESTIÓN DE LIBROS ---\n" +
                            "1. Añadir un nuevo libro\n" +
                            "2. Listar todos los libros\n" +
                            "3. Buscar libro por ID\n" +
                            "4. Actualizar un libro\n" +
                            "5. Eliminar un libro\n" +
                            "6. Volver al Menú Principal\n\n" +
                            "Elige una opción:",
                    "LibroNova - Gestión de Libros",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion == null) {
                opcion = "6";
            }

            try {
                switch (opcion) {
                    case "1":
                        agregarLibro();
                        break;
                    case "2":
                        listarLibros();
                        break;
                    case "3":
                        buscarLibro();
                        break;
                    case "4":
                        actualizarLibro();
                        break;
                    case "5":
                        eliminarLibro();
                        break;
                    case "6":
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Opción no válida. Inténtalo de nuevo.", "Error", JOptionPane.WARNING_MESSAGE);
                        break;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El año, el stock y los IDs deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado:\n" + e.getMessage(), "Error General", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        } while (!opcion.equals("6"));
    }

    private void agregarLibro() {
        String isbn = JOptionPane.showInputDialog("ISBN:");
        String titulo = JOptionPane.showInputDialog("Título:");
        String autor = JOptionPane.showInputDialog("Autor:");
        String editorial = JOptionPane.showInputDialog("Editorial:");
        Integer anio = Integer.parseInt(JOptionPane.showInputDialog("Año de Publicación:"));
        String genero = JOptionPane.showInputDialog("Género:");
        int stockTotal = Integer.parseInt(JOptionPane.showInputDialog("Stock Total:"));
        int stockDisponible = Integer.parseInt(JOptionPane.showInputDialog("Stock Disponible:"));

        Libro nuevoLibro = new Libro(isbn, titulo, autor, editorial, anio, genero, stockTotal, stockDisponible);
        Libro libroInsertado = libroService.crearLibro(nuevoLibro);

        if (libroInsertado != null) {
            JOptionPane.showMessageDialog(null, "Libro añadido con éxito!\nID Asignado: " + libroInsertado.getId());
        } else {
            JOptionPane.showMessageDialog(null, "Error al añadir el libro.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarLibros() {
        List<Libro> libros = libroService.obtenerTodosLosLibros();
        if (libros.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay libros en el catálogo.");
        } else {
            StringBuilder sb = new StringBuilder("--- CATÁLOGO DE LIBROS ---\n\n");
            for (Libro libro : libros) {
                sb.append("ID: ").append(libro.getId())
                        .append(" | Título: ").append(libro.getTitulo())
                        .append(" | Autor: ").append(libro.getAutor())
                        .append(" | Stock: ").append(libro.getStockDisponible()).append("\n");
            }
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            JOptionPane.showMessageDialog(null, scrollPane, "Listado de Libros", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscarLibro() {
        int idBuscar = Integer.parseInt(JOptionPane.showInputDialog("Introduce el ID del libro a buscar:"));
        Optional<Libro> libroOptional = libroService.buscarLibroPorId(idBuscar);

        libroOptional.ifPresentOrElse(
                libro -> {
                    JTextArea textArea = new JTextArea(libro.toString());
                    textArea.setEditable(false);
                    JOptionPane.showMessageDialog(null, textArea, "Libro Encontrado", JOptionPane.INFORMATION_MESSAGE);
                },
                () -> JOptionPane.showMessageDialog(null, "No se encontró ningún libro con el ID " + idBuscar)
        );
    }

    private void actualizarLibro() {
        int idActualizar = Integer.parseInt(JOptionPane.showInputDialog("Introduce el ID del libro a actualizar:"));
        Optional<Libro> libroParaActualizarOpt = libroService.buscarLibroPorId(idActualizar);

        if (libroParaActualizarOpt.isPresent()) {
            Libro libroActual = libroParaActualizarOpt.get();

            String nuevoTitulo = JOptionPane.showInputDialog("Nuevo título (anterior: " + libroActual.getTitulo() + "):", libroActual.getTitulo());
            int nuevoStock = Integer.parseInt(JOptionPane.showInputDialog("Nuevo stock disponible (anterior: " + libroActual.getStockDisponible() + "):", String.valueOf(libroActual.getStockDisponible())));

            libroActual.setTitulo(nuevoTitulo);
            libroActual.setStockDisponible(nuevoStock);

            libroService.actualizarLibro(libroActual);
            JOptionPane.showMessageDialog(null, "Libro actualizado con éxito.");
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró ningún libro con el ID " + idActualizar);
        }
    }

    private void eliminarLibro() {
        int idEliminar = Integer.parseInt(JOptionPane.showInputDialog("Introduce el ID del libro a eliminar:"));
        boolean eliminado = libroService.eliminarLibro(idEliminar);
        if (eliminado) {
            JOptionPane.showMessageDialog(null, "Libro eliminado con éxito.");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar el libro (puede que el ID no exista).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
