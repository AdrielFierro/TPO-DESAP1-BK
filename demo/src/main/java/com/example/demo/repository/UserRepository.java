package com.example.demo.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Método para encontrar usuarios
    @Query("SELECT u FROM User u WHERE u.name LIKE %:contains%")
    List<User> findUsers(@Param("contains") String contains, Pageable pageable);

    // Método para obtener comentarios x User Id
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.userId = :id_user")
    int countCommentsByUserId(Integer id_user);

    Optional<User> findByName(String name);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
