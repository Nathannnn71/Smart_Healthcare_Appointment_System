package com.medisphere.pattern;

import com.medisphere.model.*;
import java.util.*;

/**
 * Concrete Strategy: recommends specialists by matching patient symptoms
 * to relevant specialties.
 */
public class SymptomBasedRecommendation implements RecommendationStrategy {

    // Maps symptom keywords to specialties
    private static final Map<String, Specialty> SYMPTOM_MAP = new LinkedHashMap<>();

    static {
        SYMPTOM_MAP.put("chest", Specialty.CARDIOLOGY);
        SYMPTOM_MAP.put("heart", Specialty.CARDIOLOGY);
        SYMPTOM_MAP.put("palpitation", Specialty.CARDIOLOGY);
        SYMPTOM_MAP.put("headache", Specialty.NEUROLOGY);
        SYMPTOM_MAP.put("migraine", Specialty.NEUROLOGY);
        SYMPTOM_MAP.put("seizure", Specialty.NEUROLOGY);
        SYMPTOM_MAP.put("dizziness", Specialty.NEUROLOGY);
        SYMPTOM_MAP.put("joint", Specialty.ORTHOPEDICS);
        SYMPTOM_MAP.put("bone", Specialty.ORTHOPEDICS);
        SYMPTOM_MAP.put("fracture", Specialty.ORTHOPEDICS);
        SYMPTOM_MAP.put("back pain", Specialty.ORTHOPEDICS);
        SYMPTOM_MAP.put("skin", Specialty.DERMATOLOGY);
        SYMPTOM_MAP.put("rash", Specialty.DERMATOLOGY);
        SYMPTOM_MAP.put("acne", Specialty.DERMATOLOGY);
        SYMPTOM_MAP.put("itch", Specialty.DERMATOLOGY);
        SYMPTOM_MAP.put("child", Specialty.PEDIATRICS);
        SYMPTOM_MAP.put("fever", Specialty.GENERAL_PRACTICE);
        SYMPTOM_MAP.put("vision", Specialty.OPHTHALMOLOGY);
        SYMPTOM_MAP.put("eye", Specialty.OPHTHALMOLOGY);
        SYMPTOM_MAP.put("ear", Specialty.ENT);
        SYMPTOM_MAP.put("throat", Specialty.ENT);
        SYMPTOM_MAP.put("mood", Specialty.PSYCHIATRY);
        SYMPTOM_MAP.put("anxiety", Specialty.PSYCHIATRY);
        SYMPTOM_MAP.put("depression", Specialty.PSYCHIATRY);
    }

    @Override
    public List<Doctor> recommend(Patient patient, List<Doctor> allDoctors) {
        Set<Specialty> matchedSpecialties = new HashSet<>();

        for (String symptom : patient.getSymptoms()) {
            String lower = symptom.toLowerCase();
            for (Map.Entry<String, Specialty> entry : SYMPTOM_MAP.entrySet()) {
                if (lower.contains(entry.getKey())) {
                    matchedSpecialties.add(entry.getValue());
                }
            }
        }

        // If no match, suggest General Practice
        if (matchedSpecialties.isEmpty()) matchedSpecialties.add(Specialty.GENERAL_PRACTICE);

        List<Doctor> recommended = new ArrayList<>();
        for (Doctor d : allDoctors) {
            if (matchedSpecialties.contains(d.getSpecialty())) {
                recommended.add(d);
            }
        }

        // Sort by rating descending
        recommended.sort((a, b) -> Double.compare(b.getRating(), a.getRating()));
        return recommended;
    }

    @Override
    public String getStrategyName() { return "Symptom-Based Recommendation"; }

    @Override
    public String getStrategyDescription() {
        return "Matches your symptoms to relevant specialists using our AI symptom analysis engine.";
    }
}
