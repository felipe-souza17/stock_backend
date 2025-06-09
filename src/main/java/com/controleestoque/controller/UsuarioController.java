package com.controleestoque.controller;

import com.controleestoque.dto.UsuarioRegisterDTO;
import com.controleestoque.dto.UsuarioResponseDTO;
import com.controleestoque.model.Usuario;
import com.controleestoque.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody @Valid UsuarioRegisterDTO data) {
        Map<String, String> response = new HashMap<>();

        if (this.usuarioRepository.findByUsername(data.username()).isPresent()) {
            response.put("message", "Nome de usuário já existe.");
            return ResponseEntity.badRequest().body(response);
        }

        String encryptedPassword = passwordEncoder.encode(data.password());
        Usuario novoUsuario = new Usuario(data.username(), encryptedPassword, data.role());

        this.usuarioRepository.save(novoUsuario);
        response.put("message", "Usuário registrado com sucesso!");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> getAllUsers() {
        List<UsuarioResponseDTO> usuariosDTO = usuarioRepository.findAll().stream()
            .map(usuario -> new UsuarioResponseDTO(usuario.getId(), usuario.getUsername(), usuario.getRole()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUserById(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> new UsuarioResponseDTO(usuario.getId(), usuario.getUsername(), usuario.getRole()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Valid UsuarioRegisterDTO data) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        Map<String, String> response = new HashMap<>();
        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = optionalUsuario.get();
        if (data.password() != null && !data.password().isEmpty()) {
             usuario.setPassword(passwordEncoder.encode(data.password()));
        }
       
        usuario.setUsername(data.username());
        usuario.setRole(data.role());

        this.usuarioRepository.save(usuario);
        response.put("message", "Usuário atualizado com sucesso!");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}