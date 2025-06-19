package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRecipe;

    @Column
    private String title;

    @Column
    private int puntaje;

    @ElementCollection
    private List<String> image;

    @Column(nullable = false)
    private Integer userId;

    // esto lo podemos sacar, no va
    @Column
    private String process;

    @Column
    private Status status;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Ingredient> ingredientes;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Paso> pasos;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column
    private int tiempoReceta;

}
