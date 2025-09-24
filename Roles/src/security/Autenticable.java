package security;
public interface Autenticable {

    // Valida las credenciales de un usuario.
    boolean login(String password);

}
