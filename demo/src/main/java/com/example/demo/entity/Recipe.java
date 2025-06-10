package com.example.demo.entity;

<<<<<<< HEAD
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

=======
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Recipe {

    public Recipe() {
    }

>>>>>>> origin/master
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRecipe;

    @Column
    private String title;

<<<<<<< HEAD
    @ElementCollection
    private List<String> image;
=======
    @Column
    private String image;
>>>>>>> origin/master

    @Column(nullable = false)
    private Integer userId;

    @Column
    private String process;

<<<<<<< HEAD
    @Column
    private Status status;

    @ElementCollection
    private List<Ingredient> ingredientes;

    @Column(nullable = false)
    private LocalDateTime fecha;

=======
>>>>>>> origin/master
}
