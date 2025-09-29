package com.minitienda.Controller;

import com.minitienda.Entity.Product;
import com.minitienda.Service.InventoryServicio;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.List;

public class MenuController {
    private final InventoryServicio servicio;
    private int altas = 0;
    private int bajas = 0;
    private int actualizaciones = 0;

    public MenuController() {
        this.servicio = new InventoryServicio();
    }


    public void agregarProducto() {
        try {
            String nombre = JOptionPane.showInputDialog("Nombre del producto: ");
            if (nombre == null || nombre.trim().isEmpty()) return;

            double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio del producto:"));
            int stock = Integer.parseInt(JOptionPane.showInputDialog("Stock inicial:"));

            if (precio <= 0 || stock < 0) {
                JOptionPane.showMessageDialog(null, "Precio y stock deben ser valores positivos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            servicio.agregarProducto(nombre, precio, stock);
            JOptionPane.showMessageDialog(null, "Producto agregado con éxito.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
            altas++;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Precio y stock deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(null, "Error: Ya existe un producto con ese nombre.", "Error de Duplicado", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error de base de datos: " + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void listarInventario() {
        List<Product> productos = servicio.listarInventario();
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El inventario está vacío.", "Inventario", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("--- INVENTARIO ACTUAL ---\n");
        for (Product p : productos) {
            sb.append(p.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Inventario", JOptionPane.INFORMATION_MESSAGE);
    }

    public void actualizarPrecio() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("ID del producto a actualizar:"));
            Product producto = servicio.buscarProductoPorId(id);
            if (producto == null) {
                JOptionPane.showMessageDialog(null, "No se encontró un producto con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double nuevoPrecio = Double.parseDouble(JOptionPane.showInputDialog("Producto: " + producto.getNombre() + "\nIngrese el nuevo precio:"));
            if (nuevoPrecio <= 0) {
                JOptionPane.showMessageDialog(null, "El precio debe ser un valor positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            servicio.actualizarPrecio(id, nuevoPrecio);
            JOptionPane.showMessageDialog(null, "Precio actualizado con éxito.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
            actualizaciones++;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID y precio deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de base de datos: " + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarStock() throws SQLException {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("ID del producto a actualizar:"));
            Product producto = servicio.buscarProductoPorId(id);
            if (producto == null) {
                JOptionPane.showMessageDialog(null, "No se encontró un producto con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int nuevoStock = Integer.parseInt(JOptionPane.showInputDialog("Producto: " + producto.getNombre() + "\nIngrese el nuevo stock:"));
            if (nuevoStock < 0) {
                JOptionPane.showMessageDialog(null, "El stock no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            servicio.actualizarStock(id, nuevoStock);
            JOptionPane.showMessageDialog(null, "Stock actualizado con éxito.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
            actualizaciones++;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID y stock deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarProducto() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("ID del producto a eliminar:"));
            Product producto = servicio.buscarProductoPorId(id);
            if (producto == null) {
                JOptionPane.showMessageDialog(null, "No se encontró un producto con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres eliminar '" + producto.getNombre() + "'?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                servicio.eliminarProducto(id);
                JOptionPane.showMessageDialog(null, "Producto eliminado con éxito.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                bajas++;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de base de datos: " + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void buscarProducto() {
        String busqueda = JOptionPane.showInputDialog("Introduce el nombre o parte del nombre a buscar:");
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            List<Product> resultados = servicio.buscarPorNombre(busqueda);
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No se encontraron productos.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            StringBuilder sb = new StringBuilder("--- PRODUCTOS ENCONTRADOS ---\n");
            for (Product p : resultados) {
                sb.append(p.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Resultados de Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void mostrarResumenFinal() {
        String resumen = String.format("""
            --- RESUMEN DE LA SESIÓN ---
            Productos agregados: %d
            Productos eliminados: %d
            Productos actualizados: %d
            
            ¡Gracias por usar la Mini Tienda!
            """, altas, bajas, actualizaciones);
        JOptionPane.showMessageDialog(null, resumen, "Resumen Final", JOptionPane.INFORMATION_MESSAGE);
    }
}