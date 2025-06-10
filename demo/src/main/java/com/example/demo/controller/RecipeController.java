package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.controller.dto.RecipeDTO;
import com.example.demo.entity.Ingredient;
import com.example.demo.entity.Recipe;
import com.example.demo.entity.User;
import com.example.demo.entity.Status;
import com.example.demo.service.ImageService;
import com.example.demo.service.IngredientService;
import com.example.demo.service.RecipeService;
import com.example.demo.service.UserService;

import io.jsonwebtoken.io.IOException;

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

    @Autowired
    private IngredientService ingredientService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Recipe> createRecipe(@RequestHeader("Authorization") String authorizationHeader,
            @ModelAttribute RecipeDTO recipeDTO)
            throws IOException {

        Integer userId = userService.getIdfromToken(authorizationHeader);

        // ArrayList<String> urls = imageService.fileToURL(recipeDTO.getImagesPost());

        ArrayList<Ingredient> ingredients = recipeDTO.getIngredientes();

        LocalDateTime fecha = LocalDateTime.now();

        // Recipe recipe =
        // Recipe.builder().title(recipeDTO.getTitle()).process(recipeDTO.getProcess()).image(urls)
        // .fecha(fecha)
        // .status(Status.PENDIENTE)
        // .userId(userId).build();

        Recipe recipe = Recipe.builder().title(recipeDTO.getTitle()).process(recipeDTO.getProcess())
                .fecha(fecha)
                .status(Status.PENDIENTE)
                .userId(userId).build();

        // Recipe recipe = recipeService.addIngredientes(recipeSin.getIdRecipe(),
        // ingredients);

        Recipe createdRecipe = recipeService.createRecipe(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
    }

}
