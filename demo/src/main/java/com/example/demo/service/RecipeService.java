package com.example.demo.service;

import java.io.IOException;
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

    @Autowired
    private ImageService imageService;

    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public ArrayList<Paso> pasosDTOaPasos(ArrayList<PasoDTO> pasosDTO) {
        ArrayList<Paso> pasos = new ArrayList<>();

        for (PasoDTO pasoDTO : pasosDTO) {
            String urlImagen = null;
            try {
                if (pasoDTO.getImagen() != null && !pasoDTO.getImagen().isEmpty()) {
                    urlImagen = imageService.uploadImage(
                            pasoDTO.getImagen().getOriginalFilename(),
                            pasoDTO.getImagen().getInputStream(),
                            pasoDTO.getImagen().getSize());
                }
            } catch (IOException e) {
                throw new RuntimeException("Error al subir la imagen del paso: " + pasoDTO.getNombrePaso(), e);
            }

            Paso paso = Paso.builder()
                    .nombrePaso(pasoDTO.getNombrePaso())
                    .Proceso(String.valueOf(pasoDTO.getProceso()))
                    .url(urlImagen)
                    .build();

            pasos.add(paso);
        }

        return pasos;
    }
}
