package com.example.demo.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

import com.example.demo.entity.Ingredient;
import com.example.demo.entity.Paso;
import com.example.demo.entity.Status;

@Data
@Builder
public class RecipeDTO {
    private String title;
    private List<Ingredient> ingredientes; // [{"nombre":"Harina","medida":500,"nombreMedida":"gramos"}]
    private List<Paso> pasos; // [{"nombrePaso":"Mezclar","proceso":1}, {...}]
    private int duracion;
    private String imagePortada;
    private Status estado;
    private String motivo;
    private String autor;
}
