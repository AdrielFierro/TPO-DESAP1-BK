package com.example.demo.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    // Método para obtener todos los posts de un usuario específico
    // @Query("SELECT p FROM Post p WHERE p.userId = :userId")
    // List<Post> getPostsByUser(@Param("userId") Integer userId);
    List<Recipe> findByUserId(Integer userId);

    /*
     * // Método para obtener todos los posts de los usuarios seguidos por un
     * usuario
     * 
     * @Query("SELECT p FROM Post p WHERE p.userId IN (SELECT followed.id FROM User user JOIN user.followed followed WHERE user.id = :userId) ORDER BY fecha desc"
     * )
     * List<Post> getTimeline(@Param("userId") Integer userId);
     */
    List<Recipe> findAllByUserIdIn(Set<Integer> userIds);

}
