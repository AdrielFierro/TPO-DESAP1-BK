package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.controller.config.JwtService;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwts;

    public Integer getIdfromToken(String accessToken) {

        accessToken = accessToken.substring(7); // Eliminar "Bearer "
        int idusuario = jwts.extractId(accessToken);

        return idusuario;

    }

    public List<User> getAllUsers(String contains, int skip, int limit, String orderby) {
        Pageable pageable = PageRequest.of(skip / limit, limit, Sort.by(orderby));
        return userRepository.findUsers(contains, pageable);
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Integer userId, User user) {
        // Lógica para actualizar un usuario
        return userRepository.save(user);
    }

    public User confirmEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        user.setStatus(true);
        User confirmedEmailUser = userRepository.save(user);
        return confirmedEmailUser;
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    public int getUserCommentsCount(Integer userId) {
        // Lógica para contar los comentarios de un usuario
        return userRepository.countCommentsByUserId(userId);
    }

    // Método para obtener los seguidores de un usuario
    public Set<User> getFollowers(Integer userId) {
        return userRepository.getFollowers(userId);
    }

    // Método para obtener los usuarios seguidos por un usuario
    public Set<User> getFollowed(Integer userId) {
        return userRepository.getFollowed(userId);
    }

    @Transactional
    public User followUser(Integer userId, Integer followUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User userToFollow = userRepository.findById(followUserId)
                .orElseThrow(() -> new RuntimeException("User to follow not found"));

        if (user.getFollowed().contains(userToFollow)) {
            throw new IllegalArgumentException("User is already followed");
        }

        user.getFollowed().add(userToFollow);
        userToFollow.getFollowers().add(user);

        userRepository.save(user);
        userRepository.save(userToFollow);

        return userToFollow; // Devuelve el usuario seguido
    }

    public boolean isEmailUsed(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isUsernameUsed(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean isEmailconfirmed(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User to follow not found"));

        return user.getStatus();
    }

}
