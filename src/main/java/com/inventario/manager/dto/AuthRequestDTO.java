package com.inventario.manager.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthRequestDTO {

    @NotBlank(message = "El usuario es requerido")
    private String username;

    @NotBlank(message = "La contrasena es requerida")
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
