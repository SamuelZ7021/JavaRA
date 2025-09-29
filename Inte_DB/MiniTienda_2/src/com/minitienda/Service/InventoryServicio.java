package com.minitienda.Service;

import com.minitienda.Entity.Product;
import com.minitienda.Model.ProductoModel;

import java.sql.SQLException;
import java.util.List;

public class InventoryServicio implements ServicioInterface{
    private final ProductoModel productoModel;
    // Constructor de la clase
    public InventoryServicio() {
        this.productoModel = new ProductoModel();
    }
    // Metodo para agregar un nuevo producto pasando los argumentos que va a utilizar
    @Override
    public void agregarProducto(String nombre, double precio, int stock) throws SQLException{
        // Crear un nuevo producto con new Product
        Product product = new Product(nombre, precio, stock);
        this.productoModel.crear(product);
    }

    // Metodo para actualizar el precio de un producto
    @Override
    public void actualizarPrecio(int id, double nuevoPrecio) throws SQLException {
        // Lógica para actualizar precio, llamando el metodo buscarPorId
        Product product = this.productoModel.buscarPorId(id);
        // Manejo de condicionales
        if (product != null) {
            // Actualizamos el precio.
            product.setPrecio(nuevoPrecio);
            this.productoModel.actualizar(product);
        }
    }
    // Metodo para actualizar Stock buscado por el ID
    @Override
    public void actualizarStock(int id, int nuevoStock) throws SQLException {
        // Lógica para actualizar stock
        Product product = this.productoModel.buscarPorId(id);
        // Manejo de errores con condicionales
        if (product != null) {
            product.setStock(nuevoStock);
            this.productoModel.actualizar(product);
        }
    }
    // Metodo para eliminar un producto por ID.
    @Override
    public void eliminarProducto(int id) throws SQLException {
        this.productoModel.eliminar(id);
    }
    // Metodo para buscar producto por medio del nombre
    @Override
    public List<Product> buscarPorNombre(String texto) {
        return this.productoModel.buscarPorNombre(texto);
    }

    // Metodo para listar todos los productos.
    @Override
    public List<Product> listarInventario() {
        return this.productoModel.buscarTodos();
    }

    // Metodo para buscar un producto por ID
    @Override
    public Product buscarProductoPorId(int id) {
        return this.productoModel.buscarPorId(id);
    }
}