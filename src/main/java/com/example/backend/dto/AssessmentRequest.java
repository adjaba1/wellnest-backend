package com.example.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentRequest {

    @NotNull
    @Size(min = 10, max = 10, message = "All 10 questions must be answered")
    private Map<String, Integer> answers; // questionId -> answerValue (0-4)
}