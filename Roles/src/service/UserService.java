package service;

import config.Conexion;
import model.Administrador;
import model.Cliente;
import model.User;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del servicio de usuarios adaptada a la base de datos normalizada.
 * Utiliza transacciones y JOINs para gestionar la información en las tablas relacionadas.
 */
public class UserService implements UsuarioInterface {
    /**
     * Registra un nuevo usuario en la base de datos de forma transaccional.
     * Esto asegura que si el usuario es un cliente, se inserte en ambas tablas
     * (`usuarios` y `clientes`) de forma atómica. Si algo falla, no se guarda nada.
     */
    @Override
    public void registrarUsuario(User usuario) {
        // Se declara la conexión fuera del try para que sea accesible en los bloques catch y finally.
        Connection conn = null;
        try {
            // 1. OBTENER CONEXIÓN Y DESACTIVAR AUTO-COMMIT
            // Se obtiene una conexión a la base de datos desde la clase de utilidad.
            conn = Conexion.getConnection();
            // Se desactiva el modo "auto-commit" para iniciar una transacción manual.
            // Esto nos permite agrupar múltiples operaciones SQL como una sola unidad de trabajo.
            conn.setAutoCommit(false);

            // 2. PREPARAR Y EJECUTAR LA INSERCIÓN EN LA TABLA 'usuarios'
            // Se define la consulta SQL para insertar en la tabla principal de usuarios.
            String sqlUsuario = "INSERT INTO usuarios (nombre, email, password, rol_id, estado) VALUES (?, ?, ?, ?, ?)";

            // Se usa un "try-with-resources" para asegurar que el PreparedStatement se cierre automáticamente.
            // Statement.RETURN_GENERATED_KEYS le indica a JDBC que queremos recuperar el ID autogenerado.
            try (PreparedStatement pstmtUsuario = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {

                // Se asignan los valores del objeto 'usuario' a los placeholders (?) de la consulta.
                pstmtUsuario.setString(1, usuario.getName());
                pstmtUsuario.setString(2, usuario.getEmail());
                pstmtUsuario.setString(3, usuario.getPassword());
                pstmtUsuario.setBoolean(5, usuario.isEstado());

                // Se determina el 'rol_id' basándose en el tipo de objeto (1 para Admin, 2 para Cliente).
                int rolId = (usuario instanceof Administrador) ? 1 : 2;
                pstmtUsuario.setInt(4, rolId);

                // Se ejecuta la inserción en la tabla 'usuarios'.
                pstmtUsuario.executeUpdate();

                // 3. OBTENER EL ID GENERADO Y, SI ES CLIENTE, INSERTAR EN LA TABLA 'clientes'
                // Se obtiene el conjunto de resultados que contiene las claves generadas (en este caso, el ID).
                try (ResultSet generatedKeys = pstmtUsuario.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Se recupera el ID autoincremental (está en la primera columna).
                        int usuarioId = generatedKeys.getInt(1);
                        // Se actualiza el objeto Java con el ID asignado por la base de datos.
                        usuario.setId(usuarioId);

                        // Si el usuario es una instancia de Cliente, se procede a insertar sus datos adicionales.
                        if (usuario instanceof Cliente) {
                            String sqlCliente = "INSERT INTO clientes (usuario_id, telefono, direccion) VALUES (?, ?, ?)";
                            try (PreparedStatement pstmtCliente = conn.prepareStatement(sqlCliente)) {
                                Cliente cliente = (Cliente) usuario;
                                pstmtCliente.setInt(1, usuarioId); // Se usa el ID recién generado.
                                pstmtCliente.setString(2, cliente.getTelefono());
                                pstmtCliente.setString(3, cliente.getDireccion());
                                pstmtCliente.executeUpdate(); // Se ejecuta la inserción en la tabla 'clientes'.
                            }
                        }
                    } else {
                        // Si no se pudo obtener el ID, es un error grave y se lanza una excepción.
                        throw new SQLException("No se pudo obtener el ID del usuario creado.");
                    }
                }
            }

            // 4. CONFIRMAR LA TRANSACCIÓN
            // Si todas las operaciones SQL se ejecutaron sin errores, se confirman los cambios en la BD.
            conn.commit();

        } catch (SQLException e) {
            // 5. MANEJO DE ERRORES Y ROLLBACK
            // Si ocurre cualquier SQLException, se entra en este bloque.
            if (conn != null) {
                try {
                    // Se revierten todos los cambios hechos durante la transacción para mantener la consistencia.
                    conn.rollback();
                } catch (SQLException ex) {
                    // Si el rollback falla, se informa, aunque es un escenario poco común.
                    System.out.println("Error al intentar revertir la transacción: " + ex.getMessage());
                }
            }

            // Se verifica si el error es por una clave duplicada (ej. email ya existe).
            // El código 1062 es específico de MySQL para "Duplicate entry".
            if (e.getErrorCode() != 1062) {
                // Para cualquier otro error, se muestra un mensaje al usuario.
                JOptionPane.showMessageDialog(null, "Error de base de datos al registrar: " + e.getMessage());
            }
            // Si es un error de duplicado, se ignora silenciosamente para no molestar al cargar datos de prueba.
        } finally {
            // 6. CIERRE DE LA CONEXIÓN
            // Este bloque se ejecuta siempre, haya o no errores.
            if (conn != null) {
                try {
                    // Se restaura el modo "auto-commit" a su estado normal.
                    conn.setAutoCommit(true);
                    // Se cierra la conexión para liberar los recursos de la base de datos.
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Autentica a un usuario y devuelve su objeto correspondiente si las credenciales son correctas.
     *
     * @param email    El email del usuario que intenta iniciar sesión.
     * @param password La contraseña proporcionada.
     * Retorna un objeto User (Cliente o Administrador) si el inicio de sesión es exitoso y el usuario está activo.
     * Devuelve null si el email no existe, la contraseña es incorrecta o el usuario está bloqueado.
     */
    @Override
    public User iniciarSesion(String email, String password) {
        // 1. DEFINIR LA CONSULTA SQL CON JOINs
        // Se seleccionan todos los campos necesarios de las tres tablas.
        // - JOIN roles: Para obtener el nombre del rol ('CLIENTE' o 'ADMIN').
        // - LEFT JOIN clientes: Para obtener los datos adicionales si el usuario es un cliente.
        //   Se usa LEFT JOIN porque un administrador no tendrá una entrada en la tabla 'clientes'.
        String sql = "SELECT u.id, u.nombre, u.email, u.password, u.estado, r.nombre_rol, c.telefono, c.direccion " +
                "FROM usuarios u " +
                "JOIN roles r ON u.rol_id = r.id " +
                "LEFT JOIN clientes c ON u.id = c.usuario_id " +
                "WHERE u.email = ?";

        // Se usa try-with-resources para garantizar el cierre automático de la conexión y el PreparedStatement.
        try (Connection conn = Conexion.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Se asigna el email al placeholder de la consulta para buscar al usuario.
            pstmt.setString(1, email);

            // Se ejecuta la consulta y se obtiene el resultado.
            ResultSet rs = pstmt.executeQuery();

            // 2. PROCESAR EL RESULTADO
            // Si rs.next() es true, significa que se encontró un usuario con ese email.
            if (rs.next()) {
                String storedPassword = rs.getString("password"); // Contraseña almacenada en la BD.
                boolean estado = rs.getBoolean("estado");         // Estado (activo/bloqueado) del usuario.

                // 3. VALIDAR LA CONTRASEÑA Y EL ESTADO
                // Se crea un objeto temporal para reutilizar la lógica de validación encapsulada en el método login().
                // Este método ya comprueba tanto el estado como la contraseña.
                if (new Administrador("", "", storedPassword, estado).login(password)) {

                    // Si el login es exitoso, se recuperan todos los datos del ResultSet.
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    String nombreRol = rs.getString("nombre_rol");

                    User usuario; // Se declara el objeto User que se va a devolver.

                    // 4. CONSTRUIR EL OBJETO CORRECTO (Cliente o Administrador)
                    if ("CLIENTE".equals(nombreRol)) {
                        // Si el rol es 'CLIENTE', se crea un objeto Cliente con sus datos adicionales.
                        usuario = new Cliente(nombre, email, storedPassword, estado, rs.getString("direccion"), rs.getString("telefono"));
                    } else {
                        // Si no, se asume que es un Administrador.
                        usuario = new Administrador(nombre, email, storedPassword, estado);
                    }

                    // Se asigna el ID de la base de datos al objeto Java.
                    usuario.setId(id);
                    return usuario; // Se devuelve el objeto de usuario completamente construido.
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de base de datos al iniciar sesión: " + e.getMessage());
        }

        // Si el bucle termina sin devolver un usuario, significa que el login falló.
        return null;
    }

    /**
     * Obtiene una lista de todos los usuarios registrados en el sistema.
     * Retorna una lista de objetos User, donde cada objeto puede ser una instancia de Cliente o Administrador.
     */
    @Override
    public List<User> obtenerTodosLosUsuarios() {
        List<User> listaUsuarios = new ArrayList<>();
        // La consulta es similar a la de iniciarSesion, pero sin el WHERE para traer a todos los usuarios.
        String sql = "SELECT u.id, u.nombre, u.email, u.password, u.estado, r.nombre_rol, c.telefono, c.direccion " +
                "FROM usuarios u JOIN roles r ON u.rol_id = r.id LEFT JOIN clientes c ON u.id = c.usuario_id";

        // Se usa try-with-resources para el cierre automático de la conexión y el Statement.
        try (Connection conn = Conexion.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            // Se itera sobre cada fila del resultado de la consulta.
            while (rs.next()) {
                User usuario; // Objeto para almacenar el usuario de la fila actual.

                // Se determina si el usuario es un Cliente o Administrador basándose en el nombre del rol.
                if ("CLIENTE".equals(rs.getString("nombre_rol"))) {
                    // Si es Cliente, se crea un objeto Cliente con todos sus datos.
                    usuario = new Cliente(rs.getString("nombre"), rs.getString("email"), rs.getString("password"), rs.getBoolean("estado"), rs.getString("direccion"), rs.getString("telefono"));
                } else {
                    // De lo contrario, se crea un objeto Administrador.
                    usuario = new Administrador(rs.getString("nombre"), rs.getString("email"), rs.getString("password"), rs.getBoolean("estado"));
                }

                // Se asigna el ID de la base de datos al objeto.
                usuario.setId(rs.getInt("id"));
                // Se añade el objeto a la lista.
                listaUsuarios.add(usuario);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de base de datos al obtener usuarios: " + e.getMessage());
        }

        // Se devuelve la lista (puede estar vacía si no hay usuarios o si hubo un error).
        return listaUsuarios;
    }

    // Busca y devuelve un usuario específico por su ID.
    @Override
    public User obtenerUsuarioPorId(int id) {
        // La consulta es casi idéntica a la de iniciarSesion, pero busca por 'u.id' en lugar de 'u.email'.
        String sql = "SELECT u.id, u.nombre, u.email, u.password, u.estado, r.nombre_rol, c.telefono, c.direccion " +
                "FROM usuarios u JOIN roles r ON u.rol_id = r.id LEFT JOIN clientes c ON u.id = c.usuario_id WHERE u.id = ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id); // Se asigna el ID al placeholder de la consulta.
            ResultSet rs = pstmt.executeQuery();

            // Si se encuentra una fila, se construye el objeto correspondiente.
            if (rs.next()) {
                User usuario;
                if ("CLIENTE".equals(rs.getString("nombre_rol"))) {
                    usuario = new Cliente(rs.getString("nombre"), rs.getString("email"), rs.getString("password"), rs.getBoolean("estado"), rs.getString("direccion"), rs.getString("telefono"));
                } else {
                    usuario = new Administrador(rs.getString("nombre"), rs.getString("email"), rs.getString("password"), rs.getBoolean("estado"));
                }
                usuario.setId(rs.getInt("id"));
                return usuario;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de base de datos al buscar usuario: " + e.getMessage());
        }

        // Si no se encuentra el usuario, se devuelve null.
        return null;
    }

    /** Actualiza la información de un usuario en la base de datos de forma transaccional.
     *
     * @param usuario Se actualiza por medio del ID
     */
    @Override
    public void actualizarUsuario(User usuario) {
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            conn.setAutoCommit(false); // Inicia la transacción.

            // 1. ACTUALIZAR LA TABLA 'usuarios'
            // Se actualizan los datos comunes que están en la tabla principal.
            String sqlUsuario = "UPDATE usuarios SET nombre = ?, password = ?, estado = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUsuario)) {
                pstmt.setString(1, usuario.getName());
                pstmt.setString(2, usuario.getPassword());
                pstmt.setBoolean(3, usuario.isEstado());
                pstmt.setInt(4, usuario.getId());
                pstmt.executeUpdate();
            }

            // 2. SI ES CLIENTE, ACTUALIZAR LA TABLA 'clientes'
            // Se actualizan los datos específicos que están en la tabla de clientes.
            if (usuario instanceof Cliente) {
                String sqlCliente = "UPDATE clientes SET telefono = ?, direccion = ? WHERE usuario_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sqlCliente)) {
                    Cliente cliente = (Cliente) usuario;
                    pstmt.setString(1, cliente.getTelefono());
                    pstmt.setString(2, cliente.getDireccion());
                    pstmt.setInt(3, cliente.getId());
                    pstmt.executeUpdate();
                }
            }

            conn.commit(); // Confirma todos los cambios si no hubo errores.

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex){}
            }
            JOptionPane.showMessageDialog(null, "Error de base de datos al actualizar: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {}
            }
        }
    }


    /**
     * Elimina un usuario de la base de datos.
     * Gracias a la restricción ON DELETE CASCADE en la base de datos, al eliminar un usuario
     * de la tabla 'usuarios', sus registros asociados en 'clientes' y 'bitacora_bloqueos' se eliminan automáticamente.
     */
    @Override
    public void eliminarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de base de datos al eliminar: " + e.getMessage());
        }
    }

    /**
     * Gestiona el bloqueo o desbloqueo de un usuario y registra la acción en una bitácora.
     * Esta operación es transaccional para asegurar que el estado del usuario y el registro
     * en la bitácora se completen juntos o no se completen en absoluto.
     */
    public void gestionarBloqueoUsuario(Administrador admin, User usuarioAfectado, boolean bloquear) {
        // Regla de negocio: un administrador no puede bloquearse a sí mismo.
        if (admin.getId() == usuarioAfectado.getId()) {
            JOptionPane.showMessageDialog(null, "Un administrador no puede bloquearse a sí mismo.");
            return;
        }

        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            conn.setAutoCommit(false); // Inicia la transacción.

            // 1. ACTUALIZAR EL ESTADO DEL USUARIO
            // El estado es el opuesto del parámetro 'bloquear'. Si bloquear=true, estado=false.
            String sqlUpdate = "UPDATE usuarios SET estado = ? WHERE id = ?";
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
                pstmtUpdate.setBoolean(1, !bloquear);
                pstmtUpdate.setInt(2, usuarioAfectado.getId());
                pstmtUpdate.executeUpdate();
            }

            // 2. INSERTAR REGISTRO EN LA BITÁCORA
            // Se registra qué administrador realizó la acción, a quién afectó y qué acción fue.
            String sqlBitacora = "INSERT INTO bitacora_bloqueos (admin_id, usuario_afectado_id, accion) VALUES (?, ?, ?)";
            try (PreparedStatement pstmtBitacora = conn.prepareStatement(sqlBitacora)) {
                pstmtBitacora.setInt(1, admin.getId());
                pstmtBitacora.setInt(2, usuarioAfectado.getId());
                pstmtBitacora.setString(3, bloquear ? "BLOQUEO" : "DESBLOQUEO");
                pstmtBitacora.executeUpdate();
            }

            conn.commit(); // Confirma la transacción.
            JOptionPane.showMessageDialog(null, "Estado del usuario actualizado correctamente.");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) { /* Ignorar */ }
            }
            JOptionPane.showMessageDialog(null, "Error de base de datos al gestionar bloqueo: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) { /* Ignorar */ }
            }
        }
    }
}
