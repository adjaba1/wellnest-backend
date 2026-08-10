package com.example.backend.repository;

import com.example.backend.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    // Delete all assessments for a user
    void deleteByUserId(Long userId);

    // Find all assessments for a user
    List<Assessment> findByUserId(Long userId);
}