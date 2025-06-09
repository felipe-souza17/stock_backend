package com.controleestoque.dto;

import com.controleestoque.model.UsuarioRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRegisterDTO(
    @NotBlank
    String username,
    @NotBlank
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    String password,
    @NotNull
    UsuarioRole role
) {}