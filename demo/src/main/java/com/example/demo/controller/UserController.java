package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.controller.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam String contains,
            @RequestParam int skip,
            @RequestParam int limit,
            @RequestParam(required = false) String orderby) {
        List<User> users = userService.getAllUsers(contains, skip, limit, orderby);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        return user != null ? ResponseEntity.ok(userService.toDTO(user)) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    // para empezar
    @GetMapping("/test")
    public String hello() {
        return "Hola, mundo";
    }

    // Post User
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // Update User
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Integer userId, @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    // Delete User
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        return user != null ? ResponseEntity.ok(userService.toDTO(user)) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @PostMapping("/alias")
    public ResponseEntity<UserDTO> getUserByAlias(@RequestParam String alias) {
        User user = userService.getUserByalias(alias);
        return user != null ? ResponseEntity.ok(userService.toDTO(user)) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/isEmailUsed")
    public ResponseEntity<Boolean> isEmailUsed(@RequestParam String email) {
        boolean exists = userService.isEmailUsed(email);
        return ResponseEntity.ok(exists);
    }

    // is email confirmed?
    @GetMapping("/isEmailconfirmed")
    public ResponseEntity<Boolean> isEmailconfirmed(@RequestParam String email) {
        boolean exists = userService.isEmailconfirmed(email);
        return ResponseEntity.ok(exists);
    }

    // Get User by Username
    @GetMapping("/isUsernameUsed")
    public ResponseEntity<Boolean> isUsernameUsed(@RequestParam String username) {
        boolean exists = userService.isUsernameUsed(username);
        return ResponseEntity.ok(exists);
    }

    // Get User
    @GetMapping("/cantrecetas")
    public ResponseEntity<Integer> getCantRecetas(@RequestHeader("Authorization") String authHeader) {
        Integer userId = userService.getIdfromToken(authHeader);
        Integer countRecetas = userService.countRecipes(userId);
        return ResponseEntity.ok(countRecetas);
    }

}
