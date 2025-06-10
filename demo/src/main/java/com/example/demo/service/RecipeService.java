package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Ingredient;
import com.example.demo.entity.Recipe;
import com.example.demo.repository.RecipeRepository;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Recipe addIngredientes(Integer idRecipe, ArrayList<Ingredient> ingredients) {

        // Buscar el post por ID
        Recipe recipe = recipeRepository.findById(idRecipe)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));

        for (Ingredient ingredient : ingredients) {
            recipe.getIngredientes().add(ingredient);
        }

        recipeRepository.save(recipe); // Guardar la receta actualizada

        return recipe;
    }

}
