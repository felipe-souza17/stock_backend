package com.controleestoque.controller; 

import com.controleestoque.model.Categoria;
import com.controleestoque.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll(); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return categoria.map(ResponseEntity::ok) 
                        .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping 
    @ResponseStatus(HttpStatus.CREATED) 
    public Categoria createCategoria(@RequestBody Categoria categoria) {
        return categoriaRepository.save(categoria); 
    }

    
    @PutMapping("/{id}") 
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Long id, @RequestBody Categoria categoriaDetails) {
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    categoria.setNome(categoriaDetails.getNome());
                    categoria.setDescricao(categoriaDetails.getDescricao());
                    Categoria updatedCategoria = categoriaRepository.save(categoria);
                    return ResponseEntity.ok(updatedCategoria); 
                })
                .orElse(ResponseEntity.notFound().build()); 
    }

    
    @DeleteMapping("/{id}") 
    @ResponseStatus(HttpStatus.NO_CONTENT) 
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            return ResponseEntity.noContent().build(); 
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }
}