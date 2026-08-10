package com.example.backend.util;

import java.util.List;

public final class AssessmentQuestionIds {

    private AssessmentQuestionIds() {}

    // Stable question identifiers
    public static final String MOOD = "mood";
    public static final String WORTHLESSNESS = "worthlessness";
    public static final String INTEREST = "interest";
    public static final String CONCENTRATION = "concentration";
    public static final String SELF_HARM = "self_harm";
    public static final String ANXIETY = "anxiety";
    public static final String RESTLESSNESS = "restlessness";
    public static final String SLEEP = "sleep";
    public static final String OVERWHELM = "overwhelm";
    public static final String PHYSICAL_TENSION = "physical_tension";

    public static final List<String> ALL_IDS = List.of(
            MOOD, WORTHLESSNESS, INTEREST, CONCENTRATION, SELF_HARM,
            ANXIETY, RESTLESSNESS, SLEEP, OVERWHELM, PHYSICAL_TENSION
    );

    public static final int TOTAL_QUESTIONS = 10;
    public static final int MAX_SCORE = 40;
}