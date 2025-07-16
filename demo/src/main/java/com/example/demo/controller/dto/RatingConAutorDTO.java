package com.example.demo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingConAutorDTO {
    private Integer idRecipe;
    private Integer puntaje;
    private String comentario;
    private String user;  // <- solo el username
}
