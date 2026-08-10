package com.example.backend.util;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RiskCalculator {

    public AssessmentResult calculateAssessment(Map<String, Integer> answers) {
        // Validate all questions are answered
        if (answers.size() != AssessmentQuestionIds.TOTAL_QUESTIONS) {
            throw new IllegalArgumentException(
                    "All 10 questions must be answered. Found: " + answers.size()
            );
        }

        int totalScore = answers.values().stream().mapToInt(Integer::intValue).sum();
        int maxScore = AssessmentQuestionIds.MAX_SCORE;

        String riskLevel = determineRiskLevel(totalScore, maxScore);

        return new AssessmentResult(totalScore, maxScore, riskLevel);
    }

    private String determineRiskLevel(int score, int maxScore) {
        int percentage = (score * 100) / maxScore;

        if (percentage < 25) {
            return "Low";
        } else if (percentage < 50) {
            return "Moderate";
        } else if (percentage < 75) {
            return "High";
        } else {
            return "Severe";
        }
    }

    public record AssessmentResult(int score, int maxScore, String riskLevel) {}
}