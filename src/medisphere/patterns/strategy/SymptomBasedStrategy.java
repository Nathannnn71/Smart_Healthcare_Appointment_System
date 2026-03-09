package medisphere.patterns.strategy;

import medisphere.models.Doctor;
import medisphere.models.Patient;
//import medisphere.models.Specialization;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ============================================================
 * DESIGN PATTERN 4 — STRATEGY (Behavioral) — Concrete Strategy A
 * ============================================================
 * Recommends specialists by matching the patient's reported
 * symptoms against each specialisation's keyword list.
 */
public class SymptomBasedStrategy implements RecommendationStrategy {

    @Override
    public List<Doctor> recommend(Patient patient, List<Doctor> allDocs) {
        String symptoms = patient.getCurrentSymptoms().toLowerCase();
        if (symptoms.isBlank()) return allDocs;

        // Score each doctor: count matching keywords
        Map<Doctor, Integer> scores = new LinkedHashMap<>();
        for (Doctor doc : allDocs) {
            int score = 0;
            for (String kw : doc.getSpecialization().getKeywords()) {
                if (symptoms.contains(kw.toLowerCase())) score++;
            }
            scores.put(doc, score);
        }

        // Sort descending by score, then by rating for ties
        return scores.entrySet().stream()
                .sorted((e1, e2) -> {
                    int cmp = Integer.compare(e2.getValue(), e1.getValue());
                    if (cmp != 0) return cmp;
                    return Double.compare(e2.getKey().getRating(), e1.getKey().getRating());
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyName() { return "Symptom-Based Recommendation"; }
}
