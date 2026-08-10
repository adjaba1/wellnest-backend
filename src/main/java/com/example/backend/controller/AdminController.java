package com.example.backend.controller;

import com.example.backend.entity.Assessment;
import com.example.backend.repository.AssessmentRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalAssessments = assessmentRepository.count();

        List<Assessment> allAssessments = assessmentRepository.findAll();

        double averageScore = allAssessments.stream()
                .mapToInt(Assessment::getScore)
                .average()
                .orElse(0.0);
        averageScore = Math.round(averageScore * 10.0) / 10.0;

        Map<String, Integer> riskDistribution = new HashMap<>();
        riskDistribution.put("low", 0);
        riskDistribution.put("moderate", 0);
        riskDistribution.put("high", 0);
        riskDistribution.put("severe", 0);

        for (Assessment a : allAssessments) {
            String risk = a.getRiskLevel().toLowerCase();
            riskDistribution.put(risk, riskDistribution.getOrDefault(risk, 0) + 1);
        }

        long newUsersThisWeek = userRepository.countByCreatedAtAfter(Instant.now().minus(7, ChronoUnit.DAYS));

        Map<String, Object> response = new HashMap<>();
        response.put("totalUsers", totalUsers);
        response.put("totalAssessments", totalAssessments);
        response.put("averageScore", averageScore);
        response.put("riskDistribution", riskDistribution);
        response.put("newUsersThisWeek", newUsersThisWeek);

        return ResponseEntity.ok(response);
    }
}