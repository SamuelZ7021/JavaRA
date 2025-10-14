package com.bliNva.controller;

import com.bliNva.exception.BusinessException;
import com.bliNva.model.Socio;
import com.bliNva.service.SocioService;
import com.bliNva.service.SocioServiceInterface;
import com.bliNva.util.LoggerUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class SocioController {

    private final SocioServiceInterface socioService;

    public SocioController() {
        this.socioService = new SocioService();
    }

    public void gestionarSocios() {
        String opcion;
        do {
            opcion = JOptionPane.showInputDialog(
                    null,
                    "--- GESTIÓN DE SOCIOS ---\n" +
                            "1. Añadir nuevo socio\n" +
                            "2. Listar todos los socios\n" +
                            "3. Buscar socio por ID\n" +
                            "4. Actualizar un socio\n" +
                            "5. Eliminar un socio\n" +
                            "6. Volver al Menú Principal\n\n" +
                            "Elige una opción:",
                    "LibroNova - Gestión de Socios",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion == null) {
                return;
            }

            switch (opcion) {
                case "1":
                    crearSocio();
                    break;
                case "2":
                    listarSocios();
                    break;
                case "3":
                    buscarSocioPorId();
                    break;
                case "4":
                    actualizarSocio();
                    break;
                case "5":
                    eliminarSocio();
                    break;
                case "6":
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.", "Error", JOptionPane.WARNING_MESSAGE);
                    break;
            }

        } while (!opcion.equals("6"));
    }

    private void crearSocio() {
        try {
            String nombre = JOptionPane.showInputDialog("Nombre:");
            String apellido = JOptionPane.showInputDialog("Apellido:");
            String cc = JOptionPane.showInputDialog("Cédula (CC):");
            String email = JOptionPane.showInputDialog("Email:");
            String telefono = JOptionPane.showInputDialog("Teléfono:");
            String direccion = JOptionPane.showInputDialog("Dirección:");
            // El estado por defecto es 'activo' al crear un socio.
            String estado = "activo";

            Socio nuevoSocio = new Socio(nombre, apellido, cc, email, telefono, direccion, estado);

            // --- ¡AQUÍ ESTÁ LA MAGIA! ATRAPAMOS LA EXCEPCIÓN DE NEGOCIO ---
            Socio socioCreado = socioService.crearSocio(nuevoSocio);

            if (socioCreado != null) {
                JOptionPane.showMessageDialog(null, "Socio añadido con éxito!\nID Asignado: " + socioCreado.getId());
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo crear el socio por un error desconocido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (BusinessException e) {
            LoggerUtil.logWarning("Intento fallido de creación de socio: " + e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error de Negocio", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            LoggerUtil.logError("Error no controlado en la creación de socios.", e);
            JOptionPane.showMessageDialog(null, "Ocurrió un error crítico. Consulte el log para más detalles.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarSocios() {
        List<Socio> socios = socioService.obtenerTodosLosSocios();
        if (socios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay socios registrados.");
        } else {
            StringBuilder sb = new StringBuilder("--- LISTA DE SOCIOS ---\n\n");
            for (Socio socio : socios) {
                sb.append("ID: ").append(socio.getId())
                        .append(" | Nombre: ").append(socio.getNombre()).append(" ").append(socio.getApellido())
                        .append(" | CC: ").append(socio.getCc())
                        .append(" | Estado: ").append(socio.getEstado()).append("\n");
            }
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            JOptionPane.showMessageDialog(null, scrollPane, "Listado de Socios", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscarSocioPorId() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Introduce el ID del socio a buscar:"));
            Optional<Socio> socioOpt = socioService.buscarSocioPorId(id);
            socioOpt.ifPresentOrElse(
                    socio -> JOptionPane.showMessageDialog(null, "Socio encontrado:\n" + socio.toString()),
                    () -> JOptionPane.showMessageDialog(null, "No se encontró ningún socio con el ID " + id)
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, introduce un ID numérico válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarSocio() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Introduce el ID del socio a actualizar:"));
            Optional<Socio> socioOpt = socioService.buscarSocioPorId(id);

            if (socioOpt.isPresent()) {
                Socio socioActual = socioOpt.get();
                // Pedimos los nuevos datos, mostrando los actuales como valor por defecto.
                String nuevoEmail = JOptionPane.showInputDialog("Nuevo email:", socioActual.getEmail());
                String nuevoTelefono = JOptionPane.showInputDialog("Nuevo teléfono:", socioActual.getTelefono());
                String nuevaDireccion = JOptionPane.showInputDialog("Nueva dirección:", socioActual.getDireccion());
                String[] estados = {"activo", "inactivo"};
                String nuevoEstado = (String) JOptionPane.showInputDialog(null, "Selecciona el nuevo estado:",
                        "Actualizar Estado", JOptionPane.QUESTION_MESSAGE, null, estados, socioActual.getEstado());

                // Actualizamos el objeto con los nuevos datos.
                socioActual.setEmail(nuevoEmail);
                socioActual.setTelefono(nuevoTelefono);
                socioActual.setDireccion(nuevaDireccion);
                if(nuevoEstado != null) { // Si el usuario no cancela la selección de estado
                    socioActual.setEstado(nuevoEstado);
                }

                socioService.actualizarSocio(socioActual);
                JOptionPane.showMessageDialog(null, "Socio actualizado con éxito.");

            } else {
                JOptionPane.showMessageDialog(null, "No se encontró ningún socio con el ID " + id);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, introduce un ID numérico válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarSocio() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Introduce el ID del socio a eliminar:"));
            boolean eliminado = socioService.eliminarSocio(id);
            if (eliminado) {
                JOptionPane.showMessageDialog(null, "Socio eliminado con éxito.");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo eliminar el socio (ID no encontrado).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, introduce un ID numérico válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}
