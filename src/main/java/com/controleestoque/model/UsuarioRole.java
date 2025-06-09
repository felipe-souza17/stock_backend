package com.controleestoque.model;

public enum UsuarioRole {
    ADMIN("admin"),
    ESTOQUE("estoque");

    private String role;

    UsuarioRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}