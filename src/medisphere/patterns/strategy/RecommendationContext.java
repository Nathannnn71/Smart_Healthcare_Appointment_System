package medisphere.patterns.strategy;

import medisphere.data.DataStore;
import medisphere.models.Doctor;
import medisphere.models.Patient;

import java.util.List;

/**
 * ============================================================
 * DESIGN PATTERN 4 — STRATEGY (Behavioral) — Context
 * ============================================================
 * Holds a reference to the active RecommendationStrategy and
 * delegates the recommendation work to it.  Callers can swap
 * strategies at runtime without any code change.
 */
public class RecommendationContext {

    private RecommendationStrategy strategy;

    public RecommendationContext() {
        // Default strategy: symptom-based
        this.strategy = new SymptomBasedStrategy();
    }

    public RecommendationContext(RecommendationStrategy strategy) {
        this.strategy = strategy;
    }

    /** Swap the algorithm at runtime */
    public void setStrategy(RecommendationStrategy strategy) {
        this.strategy = strategy;
    }

    public RecommendationStrategy getStrategy() { return strategy; }

    /**
     * Execute the active strategy to get doctor recommendations.
     */
    public List<Doctor> getRecommendations(Patient patient) {
        List<Doctor> allDocs = DataStore.getInstance().getAllDoctors();
        return strategy.recommend(patient, allDocs);
    }

    public String getActiveStrategyName() {
        return strategy.getStrategyName();
    }
}
