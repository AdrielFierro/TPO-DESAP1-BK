package com.example.demo.entity;

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
public class Image {

    @Id
    @Column
    private Integer imageId;

    @Column
    private String path;

    @Column(name = "idPaso")
    private Integer idPaso;

}