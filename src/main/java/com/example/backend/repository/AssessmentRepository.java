package com.example.backend.repository;

import com.example.backend.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    // Delete all assessments for a user
    void deleteByUserId(Long userId);

    // Find all assessments for a user
    List<Assessment> findByUserId(Long userId);

    // ✅ Count assessments by risk level (for risk distribution)
    long countByRiskLevel(String riskLevel);

    // ✅ Get average score across all assessments
    @Query("SELECT AVG(a.score) FROM Assessment a")
    Double getAverageScore();

    // ✅ Get total number of assessments
    long count();
}