package com.example.demo.controller;

import com.example.demo.controller.dto.RecipeDTO;
import com.example.demo.controller.dto.PasoDTO;
import com.example.demo.entity.Ingredient;
import com.example.demo.entity.Paso;
import com.example.demo.entity.Recipe;
import com.example.demo.entity.Status;
import com.example.demo.service.ImageService;
import com.example.demo.service.RecipeService;
import com.example.demo.service.UserService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private ImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Recipe> createRecipe(
            @RequestHeader("Authorization") String authorizationHeader,
            @ModelAttribute RecipeDTO recipeDTO) throws IOException {

        Integer userId = userService.getIdfromToken(authorizationHeader);

        ObjectMapper mapper = new ObjectMapper();

        // Parseo ingredientes desde JSON
        List<Ingredient> ingredientes = mapper.readValue(
                recipeDTO.getIngredientesJson(),
                new TypeReference<List<Ingredient>>() {
                });

        // Parseo pasos desde JSON
        List<PasoDTO> pasosSinImagen = mapper.readValue(
                recipeDTO.getPasosJson(),
                new TypeReference<List<PasoDTO>>() {
                });

        // Asocio cada imagen con su paso
        MultipartFile[] imagenes = recipeDTO.getImagenesPasos();
        for (int i = 0; i < pasosSinImagen.size(); i++) {
            if (imagenes != null && i < imagenes.length) {
                pasosSinImagen.get(i).setImagen(imagenes[i]);
            }
        }

        // Transformar los PasoDTO en entidad Paso (usás tu método)
        ArrayList<Paso> pasos = recipeService.pasosDTOaPasos(new ArrayList<>(pasosSinImagen));

        Recipe recipe = Recipe.builder()
                .title(recipeDTO.getTitle())
                .process(recipeDTO.getProcess())
                .fecha(LocalDateTime.now())
                .ingredientes(new ArrayList<>(ingredientes))
                .pasos(pasos)
                .status(Status.PENDIENTE)
                .userId(userId)
                .build();

        Recipe createdRecipe = recipeService.createRecipe(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
    }

}
