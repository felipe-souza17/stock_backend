package com.controleestoque.model; 

import jakarta.persistence.*; 
import lombok.Data; 
import lombok.NoArgsConstructor; 
import lombok.AllArgsConstructor; 

@Entity 
@Table(name = "categoria") 
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class Categoria {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column(nullable = false) 
    private String nome;

    @Column(length = 255)
    private String descricao;

}