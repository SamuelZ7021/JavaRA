package security;

/**
 * Interfaz que define el contrato para objetos que pueden ser autenticados.
 * Cualquier clase que implemente esta interfaz debe proporcionar una lógica de login.
 */
public interface Autenticable {

    /**
     * Valida las credenciales de un usuario.
     * @param password La contraseña a verificar.
     * @return true si la autenticación es exitosa, false en caso contrario.
     */
    boolean login(String password);
}
