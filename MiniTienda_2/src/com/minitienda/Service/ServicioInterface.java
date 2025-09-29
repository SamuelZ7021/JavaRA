package com.minitienda.Service;

import com.minitienda.Entity.Product;

import java.sql.SQLException;
import java.util.List;

/**
 * Define el contrato para la lógica de negocio relacionada con el inventario.
 * Actúa como intermediario entre la UI y la capa de repositorio.
 */
public interface ServicioInterface {

    /**
     * Agrega un nuevo producto al sistema.
     * @param nombre Nombre del producto.
     * @param precio Precio del producto.
     * @param stock Stock inicial.
     */
    void agregarProducto(String nombre, double precio, int stock) throws SQLException;

    /**
     * Actualiza el precio de un producto existente.
     * @param id ID del producto a actualizar.
     * @param nuevoPrecio El nuevo precio.
     */
    void actualizarPrecio(int id, double nuevoPrecio) throws SQLException;

    /**
     * Actualiza el stock de un producto existente.
     * @param id ID del producto a actualizar.
     * @param nuevoStock El nuevo stock.
     */
    void actualizarStock(int id, int nuevoStock) throws SQLException;

    /**
     * Elimina un producto del sistema.
     * @param id ID del producto a eliminar.
     */
    void eliminarProducto(int id) throws SQLException;

    /**
     * Busca productos cuyo nombre contenga el texto proporcionado.
     * @param texto El texto a buscar en el nombre.
     * @return Una lista de productos que coinciden.
     */
    List<Product> buscarPorNombre(String texto);

    /**
     * Obtiene la lista completa de productos en el inventario.
     * @return Una lista de todos los productos.
     */
    List<Product> listarInventario();

    Product buscarProductoPorId(int id);
}
