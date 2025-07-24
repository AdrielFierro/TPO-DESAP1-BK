package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRating;

    @Column(nullable = false)
    private int idRecipe;

    @Column(nullable = false)
    private int puntaje;

    @Column(length = 1000)
    private String comentario;

    @Column
    private Status status;

    @Column(nullable = true)
    private LocalDateTime fechaAprobacion;

    @Column
    private String motivo;

    @ManyToOne
    private User user;
}
