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
    private Double puntaje;

    @Column
    private String imagePortada;

    @Column(nullable = false)
    private Integer userId;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "recipe_id") // Agrega esto para vincularlo si no usás mappedBy
    private List<Rating> puntajes;

}
