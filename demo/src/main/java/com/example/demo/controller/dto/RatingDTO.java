package com.example.demo.controller.dto;

import lombok.Data;

@Data
// public class RatingDTO {

//     private int recipeId;
//     private int puntaje;
//     private String comentario;

// }

public class RatingDTO {
    private Integer recipeId;
    private Integer puntaje;
    private String comentario;
    private String user; // ✅ Solo el username
}
