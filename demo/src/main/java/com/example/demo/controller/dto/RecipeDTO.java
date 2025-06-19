package com.example.demo.controller.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RecipeDTO {
    private String title;
    private String process;
    private String ingredientesJson; // [{"nombre":"Harina","medida":500,"nombreMedida":"gramos"}]
    private String pasosJson; // [{"nombrePaso":"Mezclar","proceso":1}, {...}]
    private MultipartFile[] imagenesPasos;
}
