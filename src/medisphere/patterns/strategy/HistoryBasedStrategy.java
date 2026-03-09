package medisphere.patterns.strategy;

import medisphere.data.DataStore;
import medisphere.models.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ============================================================
 * DESIGN PATTERN 4 — STRATEGY (Behavioral) — Concrete Strategy B
 * ============================================================
 * Recommends specialists based on the patient's past appointment
 * history — favouring specialisations they have visited before.
 */
public class HistoryBasedStrategy implements RecommendationStrategy {

    private final DataStore store = DataStore.getInstance();

    @Override
    public List<Doctor> recommend(Patient patient, List<Doctor> allDocs) {
        // Count how many past appointments the patient had with each specialisation
        Map<Specialization, Integer> specFrequency = new EnumMap<>(Specialization.class);

        for (String apptId : patient.getAppointmentIds()) {
            Appointment appt = store.getAppointmentById(apptId);
            if (appt == null) continue;
            Doctor doc = (Doctor) store.getUserById(appt.getDoctorId());
            if (doc == null) continue;
            specFrequency.merge(doc.getSpecialization(), 1, Integer::sum);
        }

        // Score doctors by how frequently the patient visited their specialisation
        Map<Doctor, Integer> scores = new LinkedHashMap<>();
        for (Doctor doc : allDocs) {
            int freq = specFrequency.getOrDefault(doc.getSpecialization(), 0);
            scores.put(doc, freq);
        }

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
    public String getStrategyName() { return "History-Based Recommendation"; }
}
