package medisphere.patterns.strategy;

import medisphere.models.Doctor;
import medisphere.models.Patient;
import medisphere.models.Appointment;
import java.util.List;

/**
 * SpecialistSearch acts as the client/context in the Strategy pattern.
 * It is responsible for searching and filtering specialists (doctors) based on criteria,
 * and delegates recommendation logic to the RecommendationStrategy interface.
 * Allows runtime swapping of strategies for flexible recommendations.
 */
public class SpecialistSearch {
    // Variables
    private RecommendationStrategy recommendation;

    // Constructor
    public SpecialistSearch(RecommendationStrategy recommendation) {
        this.recommendation = recommendation;
    }

    // Methods
    /**
     * Search for doctors using the active recommendation strategy.
     * @param patient The patient
     * @param doctors List of available doctors
     * @return List of recommended doctors
     */
    public List<Doctor> search(Patient patient, List<Doctor> doctors) {
        return recommendation.recommend(patient, doctors);
    }

    // Setter for strategy
    public void setRecommendation(RecommendationStrategy recommendation) {
        this.recommendation = recommendation;
    }
}
