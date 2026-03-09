package medisphere.patterns.strategy;

import medisphere.models.Doctor;
import medisphere.models.Patient;
import java.util.List;

/**
 * ============================================================
 * DESIGN PATTERN 4 — STRATEGY (Behavioral) — Strategy interface
 * ============================================================
 * Defines the algorithm contract for recommending specialists.
 * Concrete strategies swap the algorithm without changing the
 * client (RecommendationContext).
 */
public interface RecommendationStrategy {

    /**
     * Returns an ordered list of recommended doctors for the given patient.
     * @param patient  The patient requesting recommendations.
     * @param allDocs  Full list of available doctors.
     * @return         Ordered list of recommended doctors (best first).
     */
    List<Doctor> recommend(Patient patient, List<Doctor> allDocs);

    /** Short label shown in the UI to describe which strategy is active. */
    String getStrategyName();
}
