package com.example.demo.service;

import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.controller.auth.AuthenticationRequest;
import com.example.demo.controller.auth.AuthenticationResponse;
import com.example.demo.controller.auth.RegisterRequest;
import com.example.demo.controller.config.JwtService;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
        private final UserRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {
                var usuario = User.builder()
                                .name(request.getName())
                                .email(request.getEmail())
                                .username(request.getUsername())
                                .lastName(request.getLastname())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(request.getRole())
                                .status(false)
                                .description(request.getDescription())
                                .ubicacion(request.getUbicacion())
                                .build();
                repository.save(usuario);

                var jwtToken = jwtService.generateToken(usuario, usuario.getId()); // aca el getusuario_id te da
                                                                                   // el id para crear en el
                                                                                   // token directo del usuario
                                                                                   // recientemente generado
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .build();
        }

        public String authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getUsername(),
                                                request.getPassword()));

                var usuario = repository.findByUsername(request.getUsername())

                                .orElseThrow();
                var jwtToken = jwtService.generateToken(usuario, usuario.getId());
                return jwtToken;
        }
}
