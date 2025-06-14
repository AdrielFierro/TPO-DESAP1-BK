package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Paso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPaso;

    @Column
    private String nombrePaso;

    @Column
    private String Proceso;

    @Column
    private String url;

}
