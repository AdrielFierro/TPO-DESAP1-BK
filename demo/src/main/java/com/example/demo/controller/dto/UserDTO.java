package com.example.demo.controller.dto;

public class UserDTO {

    private Integer id;
    private String username;
    private String email;
    private String name;
    private String lastName;
    private String ubicacion;
    private String urlImage;
    private boolean status;
    private String role;
    private String description; // 🔥 AGREGADO
    // Constructor vacío
    public UserDTO() {
    }

    // Constructor completo (opcional)
    public UserDTO(Integer id, String username, String email, String name, String lastName,
                   String ubicacion, String urlImage, boolean status, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.ubicacion = ubicacion;
        this.urlImage = urlImage;
        this.status = status;
        this.role = role;

    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
