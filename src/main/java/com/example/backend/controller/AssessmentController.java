package com.example.backend.controller;

import com.example.backend.entity.Assessment;
import com.example.backend.repository.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assessments")
@CrossOrigin(origins = "*")
public class AssessmentController {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @GetMapping
    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assessment> getAssessmentById(@PathVariable Long id) {
        return assessmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<Assessment> getAssessmentsByUser(@PathVariable Long userId) {
        return assessmentRepository.findByUserId(userId);
    }

    // ✅ Dashboard stats endpoint – added to fix 404
    @GetMapping("/dashboard/{userId}")
    public ResponseEntity<?> getUserDashboardStats(@PathVariable Long userId) {
        List<Assessment> assessments = assessmentRepository.findByUserId(userId);
        if (assessments.isEmpty()) {
            Map<String, Object> empty = new HashMap<>();
            empty.put("averageScore", 0);
            empty.put("totalAssessments", 0);
            empty.put("latestRiskLevel", "Low");
            return ResponseEntity.ok(empty);
        }

        double avg = assessments.stream()
                .mapToInt(Assessment::getScore)
                .average()
                .orElse(0.0);
        avg = Math.round(avg * 10.0) / 10.0;

        // Find the latest assessment (by date – you may want to sort by assessmentDate)
        Assessment latest = assessments.stream()
                .reduce((a, b) -> a.getAssessmentDate().isAfter(b.getAssessmentDate()) ? a : b)
                .orElse(assessments.get(0));

        Map<String, Object> response = new HashMap<>();
        response.put("averageScore", avg);
        response.put("totalAssessments", assessments.size());
        response.put("latestRiskLevel", latest.getRiskLevel());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public Assessment createAssessment(@RequestBody Assessment assessment) {
        return assessmentRepository.save(assessment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        if (!assessmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        assessmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteAssessmentsByUser(@PathVariable Long userId) {
        assessmentRepository.deleteByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}