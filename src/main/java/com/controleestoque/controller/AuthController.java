package com.controleestoque.controller;

import com.controleestoque.dto.LoginRequestDTO;
import com.controleestoque.dto.UsuarioResponseDTO;
import com.controleestoque.model.Usuario;
import com.controleestoque.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // Importar PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO data) {
        Map<String, String> response = new HashMap<>();

        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(data.username());

        if (usuarioOptional.isEmpty()) {
            response.put("message", "Usuário ou senha inválidos.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Usuario usuario = usuarioOptional.get();

        if (!passwordEncoder.matches(data.password(), usuario.getPassword())) {
            response.put("message", "Usuário ou senha inválidos.");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getUsername(),
            usuario.getRole()
        );
        return ResponseEntity.ok(responseDTO);
    }
}