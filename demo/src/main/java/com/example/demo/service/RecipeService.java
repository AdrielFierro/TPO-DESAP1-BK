package com.example.demo.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.controller.dto.PasoDTO;
import com.example.demo.controller.dto.RatingDTO;
import com.example.demo.controller.dto.RecipeDTO;
import com.example.demo.entity.Paso;
import com.example.demo.entity.Rating;
import com.example.demo.entity.Recipe;
import com.example.demo.entity.Status;
import com.example.demo.entity.User;
import com.example.demo.repository.RecipeRepository;
import com.example.demo.repository.UserRepository;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserRepository userRepository;

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
                    .Proceso(String.valueOf(pasoDTO.getProceso()))
                    .url(urlImagen)
                    .build();

            pasos.add(paso);
        }

        return pasos;
    }

    public Rating rateRecipe(RatingDTO ratingDTO, Integer userId) {

        // Buscar la receta
        Recipe recipe = recipeRepository.findById(ratingDTO.getRecipeId())
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        // Obtener usuario
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar si ya existe un rating de este usuario en esta receta
        List<Rating> recetasRateadas = recipe.getPuntajes();

        Optional<Rating> existingRating = recetasRateadas.stream()
                .filter(r -> r.getUser().getId() == userId)
                .findFirst();

        Rating finalRating;

        if (existingRating.isPresent()) {

            Rating rating = existingRating.get();
            rating.setPuntaje(ratingDTO.getPuntaje());
            rating.setComentario(ratingDTO.getComentario());
            finalRating = rating;
        } else {

            Rating nuevoRating = Rating.builder()
                    .idRecipe(ratingDTO.getRecipeId())
                    .puntaje(ratingDTO.getPuntaje())
                    .comentario(ratingDTO.getComentario())
                    .user(user)
                    .build();

            recetasRateadas.add(nuevoRating);
            finalRating = nuevoRating;
        }

        // Calcular promedio
        double promedio = recipe.getPuntajes().stream()
                .mapToInt(Rating::getPuntaje)
                .average()
                .orElse(0.0);

        recipe.setPuntaje(promedio);

        // Guardar receta (con ratings actualizados o nuevos)
        recipeRepository.save(recipe);

        return finalRating;
    }

    public Recipe getRecipeById(Integer recipeId) {
        return recipeRepository.findById(recipeId).orElse(null);
    }

    @SuppressWarnings("unused")
    public Rating deleteRatingByrecipeID(Integer recipeId, int userId) {

        // Buscar la receta
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        // Obtener usuario
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar el rating correspondiente al usuario
        Rating ratingToDelete = recipe.getPuntajes().stream()
                .filter(r -> r.getUser().getId() == userId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("El usuario no ha calificado esta receta"));

        // Remover de la lista de puntajes
        recipe.getPuntajes().remove(ratingToDelete);

        // Opcional: actualizar el puntaje promedio de la receta
        if (!recipe.getPuntajes().isEmpty()) {
            double nuevoPromedio = recipe.getPuntajes().stream()
                    .mapToDouble(Rating::getPuntaje)
                    .average()
                    .orElse(0.0);
            recipe.setPuntaje(nuevoPromedio);
        } else {
            recipe.setPuntaje(0.0);
        }

        // Persistir los cambios
        recipeRepository.save(recipe);

        return ratingToDelete;

    }

    public List<Recipe> getAllRecipes() {

        return recipeRepository.findAll();
    }

    public List<RecipeDTO> getRecipesByUser(Integer userId) {
    List<Recipe> recetas = recipeRepository.findByUserId(userId);
    return recetas.stream().map(this::toRecipeDTO).toList();
    }


    public void deleteRecipe(Integer recipeId) {

        recipeRepository.deleteById(recipeId);

    }

    public Recipe editrecipe(Integer recipeId, RecipeDTO recipeDTO) {

        @SuppressWarnings("deprecation")
        Recipe recipe = recipeRepository.getById(recipeId);

        recipe.setTitle(recipeDTO.getTitle());
        recipe.setImagePortada(recipeDTO.getImagePortada());
        recipe.setIngredientes(recipeDTO.getIngredientes());
        recipe.setPasos(recipeDTO.getPasos());
        recipe.setTiempoReceta(recipeDTO.getDuracion());
        return recipeRepository.save(recipe);
    }

    public RecipeDTO approveRecipe(Integer recipeId) {

        @SuppressWarnings("deprecation")
        Recipe recipe = recipeRepository.getById(recipeId);
        LocalDateTime fecha = LocalDateTime.now();
        recipe.setStatus(Status.APROBADO);
        recipe.setFechaAprobacion(fecha);
        recipeRepository.save(recipe);

        RecipeDTO recipeDTO = this.toRecipeDTO(recipe);
        return recipeDTO;

    }

    @SuppressWarnings("deprecation")
    public RecipeDTO toRecipeDTO(Recipe recipe) {

        User user = userRepository.getById(recipe.getUserId());

        RecipeDTO recipeDTO = RecipeDTO.builder().title(recipe.getTitle()).ingredientes(recipe.getIngredientes())
                .estado(recipe.getStatus())
                .motivo(recipe.getMotivo())
                .imagePortada(recipe.getImagePortada())
                .pasos(recipe.getPasos())
                .duracion(recipe.getTiempoReceta())
                .autor(user.getUsername())
                .build();

        return recipeDTO;
    }

    public List<RecipeDTO> getRecipesByStatus(Status status) {
        List<Recipe> recipes = recipeRepository.findByStatus(status);
        return recipes.stream()
                .map(this::toRecipeDTO)
                .collect(Collectors.toList());
    }

    public List<RecipeDTO> getLast3ApprovedRecipes() {
        List<Recipe> recipes = recipeRepository.findTop3ByStatusOrderByFechaAprobacionDesc(Status.APROBADO);
        return recipes.stream().map(this::toRecipeDTO).toList();
    }

    @SuppressWarnings("deprecation")
    public RecipeDTO rejectRecipe(Integer recipeId, String motivo) {

        Recipe recipe = recipeRepository.getById(recipeId);
        recipe.setMotivo(motivo);
        recipe.setStatus(Status.RECHAZADO);
        recipeRepository.save(recipe);
        RecipeDTO recipeDTO = this.toRecipeDTO(recipe);

        return recipeDTO;
    }

    public void addFeaturedRecipe(User user, Recipe recipe) {
        List<Recipe> featured = user.getFeaturedRecipes();

        if (featured.contains(recipe))
            return;

        if (featured.size() >= 10) {
            throw new IllegalStateException("El usuario ya tiene 10 recetas destacadas.");
        }

        featured.add(recipe);
        userRepository.save(user);
    }

    public List<RecipeDTO> getAllaprobadasRecipes() {
        return recipeRepository.findByStatus(Status.APROBADO).stream()
                .map(this::toRecipeDTO)
                .toList();
    }

}
