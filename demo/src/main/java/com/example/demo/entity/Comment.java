package com.example.demo.entity;

import jakarta.persistence.*;

//import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Comment {

    public Comment() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @Column
    private Integer postId;

    @Column
    private Integer userId;

    @Column
    private String comment;

    // Puedes agregar más relaciones si es necesario
}
