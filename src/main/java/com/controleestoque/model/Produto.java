package com.controleestoque.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal; 

@Entity
@Table(name = "produto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT") 
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2) 
    private BigDecimal preco; 

    @Column(name = "quantidade_estoque", nullable = false) 
    private Integer quantidadeEstoque;

    
    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "categoria_id", referencedColumnName = "id") 
    private Categoria categoria;

    
    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "fornecedor_id", referencedColumnName = "id") 
    private Fornecedor fornecedor;

}