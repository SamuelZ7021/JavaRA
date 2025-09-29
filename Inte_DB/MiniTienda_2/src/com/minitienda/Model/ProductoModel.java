package com.minitienda.Model;

import com.minitienda.DataBase.ConfigDB;
import com.minitienda.Entity.Product;
import com.minitienda.Interface.CRUD;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoModel implements CRUD<Product> {

    //  Insertamos un nuevo producto en la base de datos
    @Override
    public void crear(Product producto) throws SQLException {
        // Plantilla de inserción de la base de datos (MySQL-Dbeaver)
        String sql = "INSERT INTO products(product_name, price, stock) VALUES (?, ?, ?)";
        // Utilizamos un try-with-resources para manejar la conexión y el PreparedStatement
        try (Connection conn = ConfigDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setDouble(2, producto.getPrecio());
            pstmt.setInt(3, producto.getStock());
            pstmt.executeUpdate();
        }
    }
    // Buscamos un producto por su ID
    @Override
    public Product buscarPorId(int id) {
        // Plantilla de Consultas a la base de datos
        String sql = "SELECT * FROM products WHERE id = ?";
        // Utilizamos un try-with-resources para manejar la conexión y el PreparedStatement
        try (Connection conn = ConfigDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Preparamos la consulta para que nos entregue el producto según su ID
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Return de prodcuto
                    return new Product(
                            rs.getInt("id"),
                            rs.getString("product_name"),
                            rs.getDouble("price"),
                            rs.getInt("stock")
                    );
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al buscar el producto por ID: " + e.getMessage());
        }
        return null;
    }

    // Lista de todos los productos
    @Override
    public List<Product> buscarTodos() {
        ArrayList<Product> productos = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY id";
        // Utilizamos un try-with-resources para manejar la conexión y el Statement
        try (Connection conn = ConfigDB.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                productos.add(new Product(
                        rs.getInt("id"),
                        rs.getString("product_name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al listar todos los productos: " + e.getMessage());
        }
        return productos;
    }

    @Override
    public void actualizar(Product producto) throws SQLException {
        // Plantilla de UPDATE de la base de datos (MySQL-Dbeaver)
        String sql = "UPDATE products SET product_name = ?, price = ?, stock = ? WHERE id = ?";
        // Utilizamos un try-with-resources para manejar la conexión y el PreparedStatement
        try (Connection conn = ConfigDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setDouble(2, producto.getPrecio());
            pstmt.setInt(3, producto.getStock());
            pstmt.setInt(4, producto.getId());
            pstmt.executeUpdate();
        }
    }

    // Eliminamos un producto de la base de datos por ID
    @Override
    public void eliminar(int id) throws SQLException {
        // Plantilla de DELETE de la base de datos (MySQL-Dbeaver)
        String sql = "DELETE FROM products WHERE id = ?";
        // Utilizamos un try-with-resources para manejar la conexión y el PreparedStatement
        try (Connection conn = ConfigDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    // Buscamos un producto por su nombre
    public List<Product> buscarPorNombre(String nombre){
        // Inicializamos una Lista para mostrar los productos
        List<Product> products = new ArrayList<>();
        // Consulta de SELECT de la base de datos (MySQL-Dbeaver)
        String sql = "SELECT * FROM products WHERE product_name LIKE ?";
        // Utilizamos un try-with-resources para manejar la conexión y el PreparedStatement
        try (Connection conn = ConfigDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nombre + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(new Product(
                            rs.getInt("id"),
                            rs.getString("product_name"),
                            rs.getDouble("price"),
                            rs.getInt("stock")
                    ));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al buscar productos por nombre: " + e.getMessage());
        }
        return products;
    }
}