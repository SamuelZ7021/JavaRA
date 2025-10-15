package com.libroNova.controller;

import com.libroNova.exception.BusinessException;
import com.libroNova.interfas.PartnerServiceInterface;
import com.libroNova.model.Partner;
import com.libroNova.service.PartnerService;
import com.libroNova.util.LoggerUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class PartnerController {

    private final PartnerServiceInterface socioService;

    public PartnerController() {
        this.socioService = new PartnerService();
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

    public void crearSocio() {
        try {
            String nombre = JOptionPane.showInputDialog("Nombre:");
            String apellido = JOptionPane.showInputDialog("Apellido:");
            String cc = JOptionPane.showInputDialog("Cédula (CC):");
            String email = JOptionPane.showInputDialog("Email:");
            String password = JOptionPane.showInputDialog("Password");
            String telefono = JOptionPane.showInputDialog("Teléfono:");
            String direccion = JOptionPane.showInputDialog("Dirección:");
            String estado = "activo";

            Partner nuevoPartner = new Partner(nombre, apellido, cc, email, password, telefono, direccion, estado);

            Partner partnerCreado = socioService.crearSocio(nuevoPartner);

            if (partnerCreado != null) {
                JOptionPane.showMessageDialog(null, "Socio añadido con éxito!\nID Asignado: " + partnerCreado.getId());
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
        List<Partner> partners = socioService.obtenerTodosLosSocios();
        if (partners.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay socios registrados.");
        } else {
            StringBuilder sb = new StringBuilder("--- LISTA DE SOCIOS ---\n\n");
            for (Partner partner : partners) {
                sb.append("ID: ").append(partner.getId())
                        .append(" | Nombre: ").append(partner.getNombre()).append(" ").append(partner.getApellido())
                        .append(" | CC: ").append(partner.getCc())
                        .append(" | Estado: ").append(partner.getEstado()).append("\n");
            }
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            JOptionPane.showMessageDialog(null, scrollPane, "Listado de Socios", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscarSocioPorId() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Introduce el ID del socio a buscar:"));
            Optional<Partner> socioOpt = socioService.buscarSocioPorId(id);
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
            Optional<Partner> socioOpt = socioService.buscarSocioPorId(id);

            if (socioOpt.isPresent()) {
                Partner partnerActual = socioOpt.get();
                String nuevoEmail = JOptionPane.showInputDialog("Nuevo email:", partnerActual.getEmail());
                String nuevoTelefono = JOptionPane.showInputDialog("Nuevo teléfono:", partnerActual.getTelefono());
                String nuevaDireccion = JOptionPane.showInputDialog("Nueva dirección:", partnerActual.getDireccion());
                String[] estados = {"activo", "inactivo"};
                String nuevoEstado = (String) JOptionPane.showInputDialog(null, "Selecciona el nuevo estado:",
                        "Actualizar Estado", JOptionPane.QUESTION_MESSAGE, null, estados, partnerActual.getEstado());

                partnerActual.setEmail(nuevoEmail);
                partnerActual.setTelefono(nuevoTelefono);
                partnerActual.setDireccion(nuevaDireccion);
                if(nuevoEstado != null) {
                    partnerActual.setEstado(nuevoEstado);
                }

                socioService.actualizarSocio(partnerActual);
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
            // Aquí se podría añadir una regla de negocio para no eliminar socios con préstamos activos.
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