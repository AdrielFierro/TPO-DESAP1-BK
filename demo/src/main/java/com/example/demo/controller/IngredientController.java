package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.dto.IngredientDTO;
import com.example.demo.entity.Ingredient;
import com.example.demo.service.IngredientService;
import com.example.demo.service.UserService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/recipes/{recipeId}/ingredients")
public class IngredientController {

    @Autowired
    UserService userService;

    @Autowired
    IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<Ingredient> postMethodName(@RequestBody String entity, @PathVariable Integer recipeId,
            @RequestBody IngredientDTO ingredientDTO, @RequestHeader("Authorization") String authorizationHeader) {

        Integer userId = userService.getIdfromToken(authorizationHeader);

        Ingredient createdIngredient = ingredientService.createdIngredient(recipeId, ingredientDTO, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdIngredient);
    }

}
