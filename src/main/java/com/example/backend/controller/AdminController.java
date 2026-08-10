package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    // 📊 GET /api/admin/stats
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = userService.getAdminStats();
        return ResponseEntity.ok(stats);
    }

    // 👥 GET /api/admin/users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // ⬆️ PUT /api/admin/users/{id}/promote
    @PutMapping("/users/{id}/promote")
    public ResponseEntity<Void> promoteUser(@PathVariable Long id) {
        userService.promoteUser(id);
        return ResponseEntity.ok().build();
    }

    // ⬇️ PUT /api/admin/users/{id}/demote
    @PutMapping("/users/{id}/demote")
    public ResponseEntity<Void> demoteUser(@PathVariable Long id) {
        userService.demoteUser(id);
        return ResponseEntity.ok().build();
    }

    // 🗑️ DELETE /api/admin/users/{id}
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}