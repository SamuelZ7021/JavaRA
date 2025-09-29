package com.minitienda;

import com.minitienda.Controller.MenuController;
import javax.swing.JOptionPane;
import java.sql.SQLException;

public class MenuUI {
    private final MenuController controller;

    public MenuUI() {
        this.controller = new MenuController();
    }

    public void iniciar() throws SQLException {
        String menu = """
                --- MENÚ MINI TIENDA  ---
                1. Agregar producto
                2. Listar inventario
                3. Actualizar precio
                4. Actualizar stock
                5. Eliminar producto
                6. Buscar producto por nombre
                7. Salir
                """;

        while (true) {
            String opcion = JOptionPane.showInputDialog(null, menu, "Mini Tienda", JOptionPane.PLAIN_MESSAGE);
            if (opcion == null || opcion.equals("7")) {
                controller.mostrarResumenFinal();
                break;
            }

            switch (opcion) {
                case "1" -> controller.agregarProducto();
                case "2" -> controller.listarInventario();
                case "3" -> controller.actualizarPrecio();
                case "4" -> controller.actualizarStock();
                case "5" -> controller.eliminarProducto();
                case "6" -> controller.buscarProducto();
                default -> JOptionPane.showMessageDialog(null, "Opción no válida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}