package com.example.demo.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Recipe;
import com.example.demo.entity.Status;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    // Método para obtener todos los posts de un usuario específico
    // @Query("SELECT p FROM Post p WHERE p.userId = :userId")

    List<Recipe> findByUserId(Integer userId);

    List<Recipe> findAllByUserIdIn(Set<Integer> userIds);

    List<Recipe> findByStatus(Status status);

    List<Recipe> findTop3ByStatusOrderByFechaAprobacionDesc(Status status);

}
