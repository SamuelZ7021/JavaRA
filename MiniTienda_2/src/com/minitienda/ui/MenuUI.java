package com.minitienda.ui;

import com.minitienda.negocio.Inventory;
import javax.swing.JOptionPane;


public class MenuUI {
    private final Inventory inventory;

    public MenuUI() {
        this.inventory = new Inventory();
    }


    public void iniciar() {
        String menu = "--- MENÚ MINI TIENDA ---\n" +
                "1. Agregar producto\n" +
                "2. Listar inventario\n" +
                "3. Comprar producto\n" +
                "4. Estadísticas\n" +
                "5. Buscar producto\n" +
                "6. Salir";

        while (true) {
            String opcion = JOptionPane.showInputDialog(null, menu, "Mini Tienda", JOptionPane.PLAIN_MESSAGE);

            if (opcion == null || opcion.equals("6")) {
                JOptionPane.showMessageDialog(null, inventory.generarTicketFinal(), "Ticket Final", JOptionPane.INFORMATION_MESSAGE);
                break;
            }

            try {
                switch (opcion) {
                    case "1":
                        agregarProductoUI();
                        break;
                    case "2":
                        JOptionPane.showMessageDialog(null, inventory.listarInventario(), "Inventario", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case "3":
                        comprarProductoUI();
                        break;
                    case "4":
                        JOptionPane.showMessageDialog(null, inventory.getEstadisticas(), "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case "5":
                        buscarProductoUI();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Opción no válida.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void agregarProductoUI() {
        String tipo = JOptionPane.showInputDialog("¿Qué tipo de producto? (Alimento / Electrodomestico)");
        if (tipo == null || (!tipo.equalsIgnoreCase("Alimento") && !tipo.equalsIgnoreCase("Electrodomestico"))) {
            JOptionPane.showMessageDialog(null, "Tipo de producto no válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombre = JOptionPane.showInputDialog("Nombre del producto:");
        if (nombre == null || nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio del producto:"));
            int stock = Integer.parseInt(JOptionPane.showInputDialog("Stock inicial:"));

            if (precio <= 0 || stock < 0) {
                JOptionPane.showMessageDialog(null, "El precio y el stock deben ser valores positivos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (inventory.agregarProducto(tipo, nombre, precio, stock)) {
                JOptionPane.showMessageDialog(null, "Producto agregado con éxito.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "El producto '" + nombre + "' ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El precio y el stock deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void comprarProductoUI() {
        String nombre = JOptionPane.showInputDialog("Nombre del producto a comprar:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        try {
            int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Cantidad a comprar:"));
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "La cantidad debe ser positiva.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String resultado = inventory.comprarProducto(nombre, cantidad);
            JOptionPane.showMessageDialog(null, resultado, "Compra", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "La cantidad debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarProductoUI() {
        String busqueda = JOptionPane.showInputDialog("Introduce el nombre o parte del nombre a buscar:");
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            String resultado = inventory.buscarProductosPorCoincidencia(busqueda);
            JOptionPane.showMessageDialog(null, resultado, "Resultados de Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
