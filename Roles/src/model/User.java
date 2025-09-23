package model;

import security.Autenticable;

/**
 * Clase abstracta que representa a un usuario genérico del sistema.
 * Define las propiedades y comportamientos comunes a todos los usuarios.
 */
public abstract class User implements Autenticable {
    // Contador estático para generar IDs autoincrementales.
    private static int contadorId = 1;
    private int id; // ID único del usuario.
    private String name; // Nombre del usuario.
    private String email; // Email del usuario, usado para iniciar sesión.
    protected String password; // Contraseña del usuario.
    private boolean estado; // Estado del usuario (true: activo, false: bloqueado).

    /**
     * Constructor para crear una nueva instancia de Usuario.
     * @param name Nombre del usuario.
     * @param email Email del usuario.
     * @param password Contraseña del usuario.
     * @param estado Estado inicial del usuario.
     */
    public User(String name, String email, String password, boolean estado) {
        this.id = contadorId++; // Asigna un ID único y lo incrementa.
        this.name = name;
        this.email = email;
        this.password = password;
        this.estado = estado;
    }

    // --- Getters y Setters con validaciones ---

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        } else {
            System.out.println("El nombre no puede estar vacio.");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && email.contains("@")) {
            this.email = email;
        } else {
            System.out.println("Email invalido.");
        }
    }

    protected String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != null && password.length() > 6) {
            this.password = password;
        } else {
            System.out.println("La contraseña debe tener mas de 6 caracteres.");
        }
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    /**
     * Implementación del método de login de la interfaz Autenticable.
     * @param password La contraseña a verificar.
     * @return true si el usuario está activo y la contraseña es correcta, false en caso contrario.
     */
    @Override
    public boolean login(String password) {
        if (!this.estado) { // No permite iniciar sesión si el usuario está bloqueado.
            return false;
        }
        return this.password.equals(password);
    }

    /**
     * Devuelve una cadena con la información básica del perfil del usuario.
     * @return String con los detalles del usuario.
     */
    public String mostrarPerfil() {
        return "ID: " + id + "\n" +
               "Nombre: " + name + "\n" +
               "Email: " + email + "\n" +
               "Estado: " + (estado ? "Activo" : "Bloqueado");
    }

    /**
     * Método abstracto que debe ser implementado por las clases hijas.
     * @return Una cadena que describe el rol del usuario (ej. "Cliente", "Administrador").
     */
    public abstract String rolDescripcion();
}
