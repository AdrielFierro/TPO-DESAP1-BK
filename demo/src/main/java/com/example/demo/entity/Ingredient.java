package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@AllArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idIngrediente;

    @Column
    private String nombre;

    @Column
    private int medida;

    @Column
    private String nombreMedida;

    @ManyToOne
    @JoinColumn(name = "idRecipe") // Foreign Key hacia la tabla Recipe
    private Recipe receta;

    @Column // Foreign Key hacia la tabla Recipe
    private Integer usuario;

}
