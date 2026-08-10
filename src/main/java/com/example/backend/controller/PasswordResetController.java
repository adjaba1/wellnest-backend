package com.example.backend.controller;

import com.example.backend.entity.PasswordResetToken;
import com.example.backend.entity.User;
import com.example.backend.repository.PasswordResetTokenRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/password")
@CrossOrigin(origins = "*")
public class PasswordResetController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        User user = userRepository.findByEmailIgnoreCase(email).orElse(null);
        if (user == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Email not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        tokenRepository.deleteByEmail(email);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(email);
        resetToken.setExpiryDate(Instant.now().plusSeconds(15 * 60));
        resetToken.setUsed(false);

        tokenRepository.save(resetToken);

        System.out.println("🔐 Reset token for " + email + ": " + token);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset token sent");
        response.put("token", token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        PasswordResetToken resetToken = tokenRepository.findByToken(token).orElse(null);
        if (resetToken == null || resetToken.isUsed() || resetToken.getExpiryDate().isBefore(Instant.now())) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Invalid or expired token");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmailIgnoreCase(resetToken.getEmail()).orElse(null);
        if (user == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "User not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}