package com.example.demo.controller.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.example.demo.controller.dto.JwtResponse;
import com.example.demo.controller.dto.RefreshTokenRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.PasswordResetService;
import com.example.demo.service.RefreshTokenService;
import com.example.demo.controller.config.JwtService;
import com.example.demo.entity.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository RefreshTokenRepository;

    @Autowired
    private PasswordResetService pw;

    @Autowired
    private JwtService jwtservice;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {

        Optional<com.example.demo.entity.User> user = userRepository.findByUsername(request.getUsername());
        Optional<com.example.demo.entity.User> usermail = userRepository.findByEmail(request.getEmail());

        if (user.isEmpty()) {

            if (usermail.isEmpty()) {
                pw.verifyUserEmail(request.getEmail());
                return ResponseEntity.ok(service.register(request));

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mail existente");

            }

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario existente");
        }

    }

    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody AuthenticationRequest request) {
        String usernameString = request.getUsername();

        System.out.println("Username recibido: " + request.getUsername());
        System.out.println("Password recibido: " + request.getPassword());
        // Buscar el usuario en la base de datos
        Optional<User> user = userRepository.findByUsername(usernameString);

        // Verificar si el usuario existe
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        User realuser = user.get();
        int iduser = realuser.getId();

        System.out.println("Username REAL: " + realuser.getUsername());
        System.out.println("ID REAL: " + iduser);

        Optional<RefreshToken> refreshTokenSearched = RefreshTokenRepository.findByUser_id(iduser);

        RefreshToken refreshToken;
        if (refreshTokenSearched.isPresent()) {
            refreshToken = refreshTokenSearched.get(); // Aquí accedes al valor de forma segura
        } else {
            // Si no existe el refresh token, creamos uno nuevo
            refreshToken = refreshTokenService.createRefreshToken(request.getUsername());
        }

        return JwtResponse.builder()
                .accessToken(service.authenticate(request))
                .token(refreshToken.getToken()) // Usamos el refreshToken que existe o el nuevo
                .build();
    }

    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(User -> {
                    String accessToken = jwtservice.generateToken(User, User.getId());
                    return JwtResponse.builder().accessToken(accessToken).token(refreshTokenRequest.getToken())
                            .build();

                }).orElseThrow(() -> new RuntimeException("Token not in DB"));

    }

    // Only for testing
    @PostMapping("/findToken")
    public Optional<RefreshToken> findToken(@RequestBody AuthenticationRequest authenticationRequest) {

        String usernameString = authenticationRequest.getUsername();
        Optional<User> user = userRepository.findByUsername(usernameString);
        User realuser = user.get();
        int iduser = realuser.getId();

        return RefreshTokenRepository.findByUser_id(iduser);

    }

}