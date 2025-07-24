package com.example.demo.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingDTO {
    private Integer recipeId;
    private Integer puntaje;
    private String comentario;
    private String user;
}
