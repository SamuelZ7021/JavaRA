package com.minitienda.Interface;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz genérica para definir las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * para cualquier tipo de entidad del modelo.
 * @param <T> El tipo de la entidad (ej. Producto).
 */
public interface CRUD<T> {

    /**
     * Guarda un nuevo objeto en la base de datos.
     * @param objeto El objeto a crear.
     */
    void crear(T objeto) throws SQLException;

    /**
     * Busca un objeto por su ID.
     * @param id El ID del objeto a buscar.
     * @return El objeto encontrado o null si no existe.
     */
    T buscarPorId(int id) throws SQLException;

    /**
     * Devuelve una lista con todos los objetos de la tabla.
     * @return Una lista de objetos.
     */
    List<T> buscarTodos() throws SQLException;

    /**
     * Actualiza un objeto existente en la base de datos.
     * @param objeto El objeto con los datos actualizados.
     */
    void actualizar(T objeto) throws SQLException;

    /**
     * Elimina un objeto de la base de datos por su ID.
     * @param id El ID del objeto a eliminar.
     */
    void eliminar(int id) throws SQLException;
}
