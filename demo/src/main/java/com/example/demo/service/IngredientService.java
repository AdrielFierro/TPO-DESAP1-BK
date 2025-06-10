package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.controller.dto.IngredientDTO;
import com.example.demo.entity.Ingredient;
import com.example.demo.repository.IngredientRepository;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    // Post a new ingredient to a recipe
    public Ingredient createdIngredient(Integer recipeId, IngredientDTO ingredient, Integer userId) {
        String nombre = ingredient.getNombre();
        Integer medida = ingredient.getMedida();
        String nombreMedida = ingredient.getNombreMedida();

        Ingredient createdIngredient = Ingredient.builder()
                .nombre(nombre)
                .idIngrediente(recipeId)
                .usuario(userId)
                .medida(medida)
                .nombreMedida(nombreMedida)
                .build();

        return ingredientRepository.save(createdIngredient);
    }

}
