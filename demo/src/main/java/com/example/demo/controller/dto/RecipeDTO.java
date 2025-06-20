package com.example.demo.controller.dto;

import lombok.Data;

import java.util.ArrayList;

import com.example.demo.entity.Ingredient;
import com.example.demo.entity.Paso;

@Data
public class RecipeDTO {
    private String title;
    private ArrayList<Ingredient> ingredientes; // [{"nombre":"Harina","medida":500,"nombreMedida":"gramos"}]
    private ArrayList<Paso> pasos; // [{"nombrePaso":"Mezclar","proceso":1}, {...}]
    private int duracion;
    private String imagePortada;
}
