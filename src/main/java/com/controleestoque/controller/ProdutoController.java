package com.controleestoque.controller; 

import com.controleestoque.model.Produto;
import com.controleestoque.repository.ProdutoRepository;
import com.controleestoque.repository.CategoriaRepository; 
import com.controleestoque.repository.FornecedorRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    
    @GetMapping
    public ResponseEntity<Page<Produto>> getAllProdutos(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size 
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Produto> produtosPage = produtoRepository.findAll(pageable);
        return ResponseEntity.ok(produtosPage);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        return produto.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    
    @PostMapping
    public ResponseEntity<Produto> createProduto(@RequestBody Produto produto) {
        
        if (produto.getCategoria() != null && produto.getCategoria().getId() != null) {
            categoriaRepository.findById(produto.getCategoria().getId())
                    .ifPresent(produto::setCategoria); 
        }
        if (produto.getFornecedor() != null && produto.getFornecedor().getId() != null) {
            fornecedorRepository.findById(produto.getFornecedor().getId())
                    .ifPresent(produto::setFornecedor); 
        }
        Produto savedProduto = produtoRepository.save(produto);
        return new ResponseEntity<>(savedProduto, HttpStatus.CREATED);
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto(@PathVariable Long id, @RequestBody Produto produtoDetails) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    produto.setNome(produtoDetails.getNome());
                    produto.setDescricao(produtoDetails.getDescricao());
                    produto.setPreco(produtoDetails.getPreco());
                    produto.setQuantidadeEstoque(produtoDetails.getQuantidadeEstoque());

                    
                    if (produtoDetails.getCategoria() != null && produtoDetails.getCategoria().getId() != null) {
                        categoriaRepository.findById(produtoDetails.getCategoria().getId())
                                .ifPresent(produto::setCategoria);
                    } else if (produtoDetails.getCategoria() == null) { 
                        produto.setCategoria(null);
                    }

                    if (produtoDetails.getFornecedor() != null && produtoDetails.getFornecedor().getId() != null) {
                        fornecedorRepository.findById(produtoDetails.getFornecedor().getId())
                                .ifPresent(produto::setFornecedor);
                    } else if (produtoDetails.getFornecedor() == null) { 
                        produto.setFornecedor(null);
                    }

                    Produto updatedProduto = produtoRepository.save(produto);
                    return ResponseEntity.ok(updatedProduto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        if (produtoRepository.existsById(id)) {
            produtoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}