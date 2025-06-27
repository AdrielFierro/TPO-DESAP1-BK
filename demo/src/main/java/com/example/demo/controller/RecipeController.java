package com.example.demo.controller;

import com.example.demo.controller.dto.RecipeDTO;
import com.example.demo.controller.dto.RatingDTO;
import com.example.demo.entity.Rating;
import com.example.demo.entity.Recipe;
import com.example.demo.entity.Status;
import com.example.demo.entity.User;
import com.example.demo.service.RecipeService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Recipe> createRecipe(@RequestHeader("Authorization") String authorizationHeader,
            @RequestBody RecipeDTO recipeDTO)
            throws IOException {

        Integer userId = userService.getIdfromToken(authorizationHeader);

        Recipe recipe = Recipe.builder().title(recipeDTO.getTitle())
                .ingredientes(recipeDTO.getIngredientes())
                .status(Status.PENDIENTE)
                .tiempoReceta(recipeDTO.getDuracion())
                .userId(userId)
                .pasos(recipeDTO.getPasos())
                .imagePortada(recipeDTO.getImagePortada()).build();

        Recipe createdRecipe = recipeService.createRecipe(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
    }

    // postea un rating a una receta
    @PostMapping("/rating")
    public ResponseEntity<?> postRating(@RequestHeader("Authorization") String authorizationHeader,
            @RequestBody RatingDTO rating) {

        Integer userId = userService.getIdfromToken(authorizationHeader);

        if (rating.getPuntaje() < 1 || rating.getPuntaje() > 5) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("El puntaje debe estar entre 1 y 5");
        }

        Rating createdrating = recipeService.rateRecipe(rating, userId);
        createdrating.setUser(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdrating);
    }

    // te trae el rating de tu puntuacion a partir de la receta y el usuario
    @GetMapping("/myrating/{recipeId}")
    public ResponseEntity<?> getRating(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer recipeId) {

        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (recipe == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La receta no existe");
        }

        List<Rating> ratings = recipe.getPuntajes();
        Rating myrating = ratings.stream().filter(r -> r.getIdRecipe() == recipeId).findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "El rating no existe"));

        myrating.setUser(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(myrating);
    }

    @GetMapping("/ratings/{recipeId}")
    public ResponseEntity<?> getRatingsByRecipe(@PathVariable Integer recipeId) {
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (recipe == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La receta no existe");
        }

        List<Rating> ratings = recipe.getPuntajes();

        // Opcional: ocultar el usuario en cada rating
        ratings.forEach(r -> r.setUser(null));

        return ResponseEntity.ok(ratings);
    }

    @DeleteMapping("/rating/{recipeId}")
    public ResponseEntity<?> deleteRating(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer recipeId) {

        Integer userId = userService.getIdfromToken(authorizationHeader);

        Rating rating = recipeService.deleteRatingByrecipeID(recipeId, userId);

        rating.setUser(null);

        return ResponseEntity.ok().body(rating);

    }

    @GetMapping("/totalrating/{recipeId}")
    public Double getTotalRatingByRecipe(@PathVariable Integer recipeId) {
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (recipe == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La receta no existe");
        }

        return recipe.getPuntaje();
    }

    @GetMapping
    public ResponseEntity<?> getRecipes(@RequestHeader("Authorization") String authorizationHeader) {

        List<Recipe> recipes = recipeService.getAllRecipes();

        return ResponseEntity.ok().body(recipes);
    }

    // postea un rating a una receta

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<?> deleteRecipe(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer recipeId) {

        Integer userId = userService.getIdfromToken(authorizationHeader);

        Recipe recipeToDelete = recipeService.getRecipeById(recipeId);

        if (recipeToDelete == null) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receta inexistente");

        }

        if (userId == recipeToDelete.getUserId()) {

            recipeService.deleteRecipe(recipeId);

            return ResponseEntity.ok().body("Receta eliminada");

        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No sos el dueño de la receta");
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<?> putRecipe(@PathVariable Integer recipeId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody RecipeDTO recipeDTO) {

        Integer userId = userService.getIdfromToken(authorizationHeader);

        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (recipe == null) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receta inexistente");

        }

        if (userId == recipe.getUserId()) {

            Recipe recipeeditada = recipeService.editrecipe(recipeId, recipeDTO);

            return ResponseEntity.ok().body(recipeeditada);

        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No sos el dueño de la receta");

    }

    @PutMapping("aprobar/{recipeId}")
    public ResponseEntity<?> putapproveRecipe(@PathVariable Integer recipeId,
            @RequestHeader("Authorization") String authorizationHeader) {
        RecipeDTO recipeAprobada = recipeService.approveRecipe(recipeId);
        return ResponseEntity.ok().body(recipeAprobada);

    }

    @PutMapping("rechazar/{recipeId}")
    public ResponseEntity<?> putrejectRecipe(@PathVariable Integer recipeId,
            @RequestHeader("Authorization") String authorizationHeader, @RequestBody String motivo) {

        RecipeDTO recipeRechazada = recipeService.rejectRecipe(recipeId, motivo);
        return ResponseEntity.ok().body(recipeRechazada);

    }

    @GetMapping("/status/pendientes")
    public ResponseEntity<List<RecipeDTO>> getPendingRecipes(
            @RequestHeader("Authorization") String authorizationHeader) {

        return ResponseEntity.ok(recipeService.getRecipesByStatus(Status.PENDIENTE));
    }

    @GetMapping("/status/aprobados/ultimas")
    public ResponseEntity<List<RecipeDTO>> getLast3ApprovedRecipes(
            @RequestHeader("Authorization") String authorizationHeader) {

        return ResponseEntity.ok(recipeService.getLast3ApprovedRecipes());
    }

    @GetMapping("/guardadas")
    public ResponseEntity<List<RecipeDTO>> getMyFeaturedRecipes(@RequestHeader("Authorization") String authHeader) {
        Integer userId = userService.getIdfromToken(authHeader);
        User user = userService.getUserById(userId);

        List<RecipeDTO> result = user.getFeaturedRecipes().stream()
                .map(recipeService::toRecipeDTO)
                .toList();

        return ResponseEntity.ok(result);
    }

    @PutMapping("/guardadas/{recipeId}")
    public ResponseEntity<?> toggleFeaturedRecipe(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer recipeId) {

        Integer userId = userService.getIdfromToken(authorizationHeader);
        boolean added = userService.toggleFeaturedRecipe(userId, recipeId);

        String mensaje = added
                ? "Receta agregada a destacadas"
                : "Receta eliminada de destacadas";

        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/aprobadas")
    public ResponseEntity<?> getRecipesAprobadas(@RequestHeader("Authorization") String authorizationHeader) {

        List<RecipeDTO> recipes = recipeService.getAllaprobadasRecipes();

        return ResponseEntity.ok().body(recipes);
    }

}
