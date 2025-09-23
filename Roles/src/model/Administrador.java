package model;

/**
 * Clase que representa a un Administrador, un tipo de Usuario con privilegios especiales.
 * Hereda de User y añade métodos para la gestión de otros usuarios.
 */
public class Administrador extends User {

    // Constructor para crear una nueva instancia de Administrador.
    public Administrador(String name, String email, String password, boolean estado) {
        super(name, email, password, estado);
    }

    // Permite a un administrador bloquear o desbloquear a otro usuario.
    public void gestionarBloqueoUsuario(User usuario, boolean bloquear) {
        // Un administrador no puede bloquearse a sí mismo.
        if (this.getId() == usuario.getId()) {
            System.out.println("Un administrador no puede bloquearse a sí mismo.");
            return;
        }
        // El estado se establece en el opuesto de 'bloquear'.
        usuario.setEstado(!bloquear);
    }

    // Sobrescribe el método para mostrar el perfil, añadiendo la descripción del rol.
    @Override
    public String mostrarPerfil() {
        return super.mostrarPerfil() + "\n" +
               "Rol: " + rolDescripcion();
    }

    // Implementación del método abstracto para describir el rol.
    @Override
    public String rolDescripcion() {
        return "Administrador";
    }
}
