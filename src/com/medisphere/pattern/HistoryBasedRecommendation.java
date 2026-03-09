package com.medisphere.pattern;

import com.medisphere.model.*;
import java.util.*;

/**
 * Concrete Strategy: recommends specialists based on the patient's
 * past appointment history (previously visited doctors / specialties).
 */
public class HistoryBasedRecommendation implements RecommendationStrategy {

    @Override
    public List<Doctor> recommend(Patient patient, List<Doctor> allDoctors) {
        List<String> pastIds = patient.getPastDoctorIds();

        // Collect specialties from previously visited doctors
        Set<Specialty> visitedSpecialties = new HashSet<>();
        for (Doctor d : allDoctors) {
            if (pastIds.contains(d.getUserId())) {
                visitedSpecialties.add(d.getSpecialty());
            }
        }

        List<Doctor> recommended = new ArrayList<>();
        if (visitedSpecialties.isEmpty()) {
            // No history: recommend top-rated doctors
            recommended.addAll(allDoctors);
            recommended.sort((a, b) -> Double.compare(b.getRating(), a.getRating()));
            return recommended.subList(0, Math.min(4, recommended.size()));
        }

        // Include same-specialty doctors with high ratings
        for (Doctor d : allDoctors) {
            if (visitedSpecialties.contains(d.getSpecialty()) && !pastIds.contains(d.getUserId())) {
                recommended.add(d);
            }
        }

        // Also include the doctors they've seen (for follow-ups)
        for (Doctor d : allDoctors) {
            if (pastIds.contains(d.getUserId()) && !recommended.contains(d)) {
                recommended.add(0, d);
            }
        }

        recommended.sort((a, b) -> Double.compare(b.getRating(), a.getRating()));
        return recommended;
    }

    @Override
    public String getStrategyName() { return "History-Based Recommendation"; }

    @Override
    public String getStrategyDescription() {
        return "Suggests doctors based on your past appointments, care continuity, and follow-up needs.";
    }
}
