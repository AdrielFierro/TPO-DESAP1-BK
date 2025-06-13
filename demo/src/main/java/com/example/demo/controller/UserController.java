package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import java.util.List;
import java.util.Set;

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

    // Get User
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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

    // Get Comments
    @GetMapping("/{userId}/comments")
    public ResponseEntity<Integer> getUserComments(@PathVariable Integer userId) {
        int commentsCount = userService.getUserCommentsCount(userId);
        return ResponseEntity.ok(commentsCount);
    }

    // Get Followers
    @GetMapping("/{userId}/followers")
    public ResponseEntity<Set<User>> getFollowers(@PathVariable Integer userId) {
        Set<User> followers = userService.getFollowers(userId);
        if (followers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(followers);
    }

    // Get Followed
    @GetMapping("/{userId}/followed")
    public ResponseEntity<Set<User>> getFollowed(@PathVariable Integer userId) {
        Set<User> followed = userService.getFollowed(userId);
        if (followed.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(followed);
    }

    @PostMapping("/{userId}/follow/{followUserId}")
    public ResponseEntity<User> followUser(
            @PathVariable Integer userId,
            @PathVariable Integer followUserId) {
        try {
            User followedUser = userService.followUser(userId, followUserId);
            return ResponseEntity.status(HttpStatus.CREATED).body(followedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // Usuario ya seguido
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usuario no encontrado
        }
    }

    // Get User by Email
    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
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

}
