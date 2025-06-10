package com.example.demo.controller.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Ingredient;

import lombok.Data;

@Data
public class RecipeDTO {

    private String title;

    // private List<MultipartFile> imagesPost;

    private String process;

    private ArrayList<Ingredient> ingredientes;
}
