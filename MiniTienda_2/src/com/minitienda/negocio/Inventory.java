package com.minitienda.negocio;

import com.minitienda.modelo.Alimento;
import com.minitienda.modelo.Electrodomestico;
import com.minitienda.modelo.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;


public class Inventory {
    private final ArrayList<Product> productos;
    private final HashMap<String, Integer> stock;
    private final HashMap<String, Double> compras;

    public Inventory() {
        this.productos = new ArrayList<>();
        this.stock = new HashMap<>();
        this.compras = new HashMap<>();
    }

    public boolean agregarProducto(String tipo, String nombre, double precio, int cantidad) {
        if (buscarProductoPorNombreExacto(nombre) != null) {
            return false; // El producto ya existe
        }

        Product producto;
        if (tipo.equalsIgnoreCase("Alimento")) {
            producto = new Alimento(nombre, precio);
        } else {
            producto = new Electrodomestico(nombre, precio);
        }

        productos.add(producto);
        stock.put(nombre, cantidad);
        return true;
    }

    public String listarInventario() {
        if (productos.isEmpty()) {
            return "El inventario está vacío.";
        }
        StringBuilder sb = new StringBuilder("--- INVENTARIO ACTUAL ---\n");
        for (Product p : productos) {
            sb.append("Nombre: ").append(p.getNombre()).append("\n");
            sb.append("Precio: $").append(p.getPrecio()).append("\n");
            sb.append("Stock: ").append(stock.get(p.getNombre())).append(" unidades\n");
            sb.append("Descripción: ").append(p.getDescripcion()).append("\n");
            sb.append("---------------------------\n");
        }
        return sb.toString();
    }


    public String comprarProducto(String nombre, int cantidad) {
        Product producto = buscarProductoPorNombreExacto(nombre);
        if (producto == null) {
            return "Error: El producto no existe.";
        }

        int stockActual = stock.get(nombre);
        if (stockActual < cantidad) {
            return "Error: No hay suficiente stock. Disponible: " + stockActual;
        }

        stock.put(nombre, stockActual - cantidad);
        compras.put(nombre, compras.getOrDefault(nombre, 0.0) + (producto.getPrecio() * cantidad));

        return "Compra realizada con éxito: " + cantidad + " x " + nombre;
    }

    public String buscarProductosPorCoincidencia(String busqueda) {
        String resultado = productos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(busqueda.toLowerCase()))
                .map(p -> "- " + p.getNombre() + " ($ " + p.getPrecio() + ")")
                .collect(Collectors.joining("\n"));

        return resultado.isEmpty() ? "No se encontraron productos." : "Productos encontrados:\n" + resultado;
    }

    public String getEstadisticas() {
        if (productos.isEmpty()) {
            return "No hay productos para generar estadísticas.";
        }

        Product masCaro = productos.get(0);
        Product masBarato = productos.get(0);

        for (Product p : productos) {
            if (p.getPrecio() > masCaro.getPrecio()) {
                masCaro = p;
            }
            if (p.getPrecio() < masBarato.getPrecio()) {
                masBarato = p;
            }
        }

        return "--- ESTADÍSTICAS ---\n" +
                "Producto más caro: " + masCaro.getNombre() + " ($ " + masCaro.getPrecio() + ")\n" +
                "Producto más barato: " + masBarato.getNombre() + " ($ " + masBarato.getPrecio() + ")";
    }

    public String generarTicketFinal() {
        if (compras.isEmpty()) {
            return "No se realizaron compras.";
        }

        StringBuilder ticket = new StringBuilder("--- TICKET FINAL DE COMPRA ---\n");
        double total = 0;
        for (String nombre : compras.keySet()) {
            double subtotal = compras.get(nombre);
            ticket.append("Producto: ").append(nombre).append(", Total: $").append(subtotal).append("\n");
            total += subtotal;
        }
        ticket.append("--------------------------------\n");
        ticket.append("TOTAL A PAGAR: $").append(total);
        return ticket.toString();
    }


    public Product buscarProductoPorNombreExacto(String nombre) {
        for (Product p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }
}
