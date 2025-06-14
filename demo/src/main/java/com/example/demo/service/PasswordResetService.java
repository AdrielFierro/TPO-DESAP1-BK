package com.example.demo.service;

import com.example.demo.entity.User;

import com.example.demo.repository.UserRepository;

import com.example.demo.utils.EmailSender;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import com.warrenstrange.googleauth.GoogleAuthenticator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserRepository userRepository;

    private Map<String, String> resetTokens = new HashMap<>(); // Almacenar pares de email y código generado
    private Map<String, Long> tokenTimestamps = new HashMap<>(); // Almacenar tiempos de generación de TOTP
    private GoogleAuthenticator gAuth = new GoogleAuthenticator();

    // Generar un código TOTP de 4 dígitos para un usuario
    public String generateTOTP(String email) {
        int code = gAuth.getTotpPassword("userSecretKey");
        String fourDigitCode = String.format("%06d", code % 1000000);

        // Log para verificar el código generado y almacenado
        resetTokens.put(email, fourDigitCode);
        tokenTimestamps.put(email, System.currentTimeMillis());

        System.out.println("Generated TOTP for " + email + ": " + fourDigitCode);

        return fourDigitCode;
    }

    // Método para enviar el correo (aquí puedes configurar la lógica de envío)
    public void sendEmail(String to, String subject, String body) {
        EmailSender.sendEmail(to, subject, body); // Llamada al método de EmailSender
    }

    // Solicitar restablecimiento de contraseña generando TOTP y enviando un correo
    // electrónico
    public void requestPasswordReset(String email) {
        String totpCode = generateTOTP(email); // Genera el TOTP
        String subject = "Password Reset Code";
        String body = "Your TOTP code for password reset is: " + totpCode + ". This code will expire in 5 minutes.";
        sendEmail(email, subject, body); // Envía el correo
    }

    public void verifyUserEmail(String email) {
        String totpCode = generateTOTP(email); // Genera el TOTP
        String subject = "Account verification Code";
        String body = "Your TOTP code for account verification is: " + totpCode
                + ". This code will expire in 5 minutes.";
        sendEmail(email, subject, body); // Envía el correo
    }

    // Verificar si el código TOTP ingresado es correcto
    public boolean verifyTOTP(String email, String inputCode) {
        String storedCode = resetTokens.get(email);
        Long generatedTime = tokenTimestamps.get(email);
        long currentTime = System.currentTimeMillis();

        // Agregar logs para verificación
        System.out.println("Stored Code: " + storedCode);
        System.out.println("Input Code: " + inputCode);
        System.out.println("Generated Time: " + generatedTime);
        System.out.println("Current Time: " + currentTime);

        // Verificar si el código es correcto y si no ha expirado
        return storedCode != null && storedCode.equals(inputCode.trim()) &&
                (generatedTime != null && (currentTime - generatedTime < 300000)); // 5 minutos
    }

    public User resetUserPassword(User user, String password) {

        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user);
    }

}
