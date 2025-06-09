package com.controleestoque.dto;

import com.controleestoque.model.UsuarioRole;

public record UsuarioResponseDTO(
    Long id,
    String username,
    UsuarioRole role
) {}
