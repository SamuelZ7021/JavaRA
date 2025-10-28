package com.example1.demo.dtos;

import jakarta.validation.constraints.NotEmpty;

public class LoginDTO {

    @NotEmpty(message = "El nombre de usuario no puede estar vacío")
    private String username;

    @NotEmpty(message = "La contraseña no puede estar vacía")
    private String password;

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
