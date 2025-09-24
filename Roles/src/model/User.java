package model;

import security.Autenticable;

/**
 * Clase abstracta que representa a un usuario genérico del sistema.
 */
public abstract class User implements Autenticable {
    private int id; // ID único del usuario (gestionado por la base de datos).
    private String name;
    private String email;
    protected String password;
    private boolean estado;

    public User(String name, String email, String password, boolean estado) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.estado = estado;
    }

    // --- Getters y Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    /**
     * Devuelve la contraseña. Se hace público para que el servicio pueda acceder a ella.
     * @return la contraseña del usuario.
     */
    public String getPassword() {
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

    @Override
    public boolean login(String password) {
        if (!this.estado) {
            return false;
        }
        return this.password.equals(password);
    }

    public String mostrarPerfil() {
        return "ID: " + id + "\n" +
               "Nombre: " + name + "\n" +
               "Email: " + email + "\n" +
               "Estado: " + (estado ? "Activo" : "Bloqueado");
    }

    public abstract String rolDescripcion();
}
