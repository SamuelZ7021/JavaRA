package model;

/**
 * Clase que representa a un Cliente, un tipo específico de Usuario.
 * Hereda de User y añade propiedades específicas como dirección y teléfono.
 */
public class Cliente extends User {
    private String direccion; // Dirección de envío del cliente.
    private String telefono; // Número de contacto del cliente.

    // Constructor para crear una nueva instancia de Cliente.
    public Cliente(String name, String email, String password, boolean estado, String direccion, String telefono) {
        super(name, email, password, estado);
        this.direccion = direccion;
        this.telefono = telefono;
    }

    // --- Getters para propiedades específicas de Cliente ---

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    /**
     * Actualiza la información de contacto del cliente.
     * @param telefono Nuevo teléfono.
     * @param direccion Nueva dirección.
     */
    public void actualizarContacto(String telefono, String direccion) {
        if (telefono != null && !telefono.trim().isEmpty()) {
            this.telefono = telefono;
        } else {
            System.out.println("El teléfono no puede estar vacío.");
        }

        if (direccion != null && !direccion.trim().isEmpty()) {
            this.direccion = direccion;
        } else {
            System.out.println("La dirección no puede estar vacía.");
        }
    }

    // Sobrescribe el método para mostrar el perfil, añadiendo la información específica del cliente.
    @Override
    public String mostrarPerfil() {
        return super.mostrarPerfil() + "\n" +
               "Dirección: " + direccion + "\n" +
               "Teléfono: " + telefono + "\n" +
               "Rol: " + rolDescripcion();
    }

    // Implementación del método abstracto para describir el rol.
    @Override
    public String rolDescripcion() {
        return "Cliente";
    }


}
