package com.medisphere.pattern;

import com.medisphere.model.Doctor;
import com.medisphere.model.Patient;
import java.util.List;

/**
 * =========================================================
 * DESIGN PATTERN 3: STRATEGY (Behavioral)
 * =========================================================
 * Purpose: Defines a family of specialist recommendation
 * algorithms, encapsulates each one, and makes them
 * interchangeable at runtime without altering the clients.
 *
 * This interface is the Strategy role.
 * =========================================================
 */
public interface RecommendationStrategy {
    List<Doctor> recommend(Patient patient, List<Doctor> allDoctors);
    String getStrategyName();
    String getStrategyDescription();
}
