package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.AssessmentRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void promoteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setRole(User.Role.ADMIN);   // ✅ Now resolves
        userRepository.save(user);
    }

    @Transactional
    public void demoteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setRole(User.Role.USER);    // ✅ Now resolves
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        assessmentRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    public Map<String, Object> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalUsers = userRepository.count();
        stats.put("totalUsers", totalUsers);

        long totalAssessments = assessmentRepository.count();
        stats.put("totalAssessments", totalAssessments);

        Double avgScore = assessmentRepository.getAverageScore();
        stats.put("averageScore", avgScore != null ? Math.round(avgScore * 10) / 10.0 : 0);

        Instant weekAgo = LocalDate.now().minusDays(7).atStartOfDay().toInstant(ZoneOffset.UTC);
        long newThisWeek = userRepository.countByCreatedAtAfter(weekAgo);
        stats.put("newThisWeek", newThisWeek);

        Map<String, Long> riskDistribution = new HashMap<>();
        riskDistribution.put("low", assessmentRepository.countByRiskLevel("LOW"));
        riskDistribution.put("moderate", assessmentRepository.countByRiskLevel("MODERATE"));
        riskDistribution.put("high", assessmentRepository.countByRiskLevel("HIGH"));
        riskDistribution.put("severe", assessmentRepository.countByRiskLevel("SEVERE"));
        stats.put("riskDistribution", riskDistribution);

        return stats;
    }
}