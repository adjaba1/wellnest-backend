package com.example.backend.util;

import java.util.Map;
import java.util.Set;

public final class AssessmentValidator {

    private AssessmentValidator() {}

    public static void validateAnswers(Map<String, Integer> answers) {
        // 1. Check all 10 questions are present
        if (answers.size() != AssessmentQuestionIds.TOTAL_QUESTIONS) {
            throw new IllegalArgumentException(
                    "All 10 questions must be answered. Found: " + answers.size()
            );
        }

        // 2. Check keys are valid and no duplicates
        Set<String> validKeys = Set.copyOf(AssessmentQuestionIds.ALL_IDS);
        for (String key : answers.keySet()) {
            if (!validKeys.contains(key)) {
                throw new IllegalArgumentException("Invalid question ID: " + key);
            }
        }

        // 3. Check values are between 0 and 4
        for (Map.Entry<String, Integer> entry : answers.entrySet()) {
            int value = entry.getValue();
            if (value < 0 || value > 4) {
                throw new IllegalArgumentException(
                        "Answer for '" + entry.getKey() + "' must be between 0 and 4. Got: " + value
                );
            }
        }

        // 4. Check for missing keys (duplicate check)
        if (answers.keySet().size() != AssessmentQuestionIds.ALL_IDS.size()) {
            throw new IllegalArgumentException(
                    "Missing or duplicate question IDs. Expected exactly 10 unique questions."
            );
        }
    }
}