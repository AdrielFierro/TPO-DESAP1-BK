package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.controller.auth.AuthenticationRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PasswordResetService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pass")
@RequiredArgsConstructor
public class PasswordResetController {

    @Autowired
    private final UserRepository repository;
    @Autowired
    private final PasswordResetService passwordResetService;
    @Autowired
    private final UserService us;

    // Endpoint para solicitar un restablecimiento de contraseña
    @PostMapping("/request-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        User user = us.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.ok("No se encontró un usuario con este mail");
        } else {

            if (user.getStatus() == true) {
                passwordResetService.requestPasswordReset(email);
                return ResponseEntity.ok("Se envió mail de solicitud de cambio de contraseña"); // Respondemos con 200
                                                                                                // OK
            } else {

                return ResponseEntity.ok(
                        "El email no se encuentra confirmado, para poder continuar con el proceso de registro debe solicitar que se elimine el email a ratatouilletpo@gmail.com");

            }

        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyUserEmail(@RequestParam String email, @RequestParam String totpCode) {
        Boolean correctTotp = passwordResetService.verifyTOTP(email, totpCode);

        if (correctTotp == true) {
            us.confirmEmail(email);
            return ResponseEntity.ok("email confirmado");

        }
        return ResponseEntity.ok("totp incorrecto"); // Respondemos con bad request OK
    }

    // Endpoint para verificar el TOTP
    @PostMapping("/verify-totp")
    public ResponseEntity<Map<String, Boolean>> verifyTotp(@RequestParam String email, @RequestParam String totpCode) {
        // Agregar registro para depuración
        System.out.println("Email: " + email + ", TOTP Code: " + totpCode);

        boolean isValid = passwordResetService.verifyTOTP(email, totpCode);

        Map<String, Boolean> response = new HashMap<>();
        response.put("success", isValid);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(@RequestParam String email, @RequestParam String totpCode,
            @RequestParam String newpass) {

        String password = newpass;

        // Buscar el usuario en la base de datos
        Optional<User> user = repository.findByEmail(email);

        // Verificar si el usuario existe
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        User userobj = user.get();

        userobj.setPassword(password);

        User updatedUser = passwordResetService.resetUserPassword(userobj, password);

        if (updatedUser != null) {
            return ResponseEntity.ok(true);

        } else {
            return ResponseEntity.ok(false);
        }

    }
}
