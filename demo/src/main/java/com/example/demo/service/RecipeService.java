package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.controller.dto.PasoDTO;
import com.example.demo.entity.Paso;
import com.example.demo.entity.Recipe;
import com.example.demo.repository.RecipeRepository;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public ArrayList<Paso> pasosDTOaPasos(ArrayList<PasoDTO> pasosDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pasosDTOaPasos'");
    }

}
