package model;

/**
 * Clase que representa a un Administrador. Su lógica específica (como bloquear usuarios)
 * ahora es gestionada por la capa de servicio para interactuar con la base de datos.
 */
public class Administrador extends User {

    public Administrador(String name, String email, String password, boolean estado) {
        super(name, email, password, estado);
    }

    @Override
    public String mostrarPerfil() {
        return super.mostrarPerfil() + "\n" +
               "Rol: " + rolDescripcion();
    }

    @Override
    public String rolDescripcion() {
        return "Administrador";
    }
}
