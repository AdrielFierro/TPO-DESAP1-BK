package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Recipe {

    public Recipe() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRecipe;

    @Column
    private String title;

    @Column
    private String image;

    @Column(nullable = false)
    private Integer userId;

    @Column
    private String process;

}
