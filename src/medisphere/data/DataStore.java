package medisphere.data;

import medisphere.models.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ============================================================
 * DESIGN PATTERN 1 — SINGLETON (Creational)
 * ============================================================
 * Ensures exactly one DataStore instance exists throughout the
 * application lifecycle.  All data (users, appointments,
 * notifications) is stored here as in-memory collections.
 */
public class DataStore {

    // ── Singleton instance ──────────────────────────────────
    private static DataStore instance;
        private static DataStore centralDataStore;

    private final Map<String, User>         users         = new LinkedHashMap<>();
    private final Map<String, Appointment>  appointments  = new LinkedHashMap<>();
    private final List<Notification>        notifications = new ArrayList<>();

    private int idCounter = 1000;

    // Private constructor — prevents external instantiation
    private DataStore() {
        seedData();
    }

    /** Thread-safe lazy singleton accessor */
    public static synchronized DataStore getInstance() {
           if (centralDataStore == null) centralDataStore = new DataStore();
           return centralDataStore;
    }

    // ── ID generator ────────────────────────────────────────
    public String nextId(String prefix) {
        return prefix + (++idCounter);
    }

    // ── User operations ─────────────────────────────────────
    public void addUser(User user)          { users.put(user.getUserId(), user); }
    public User getUserById(String id)      { return users.get(id); }
    public Collection<User> getAllUsers()   { return users.values(); }

    public User findUserByEmail(String email) {
        return users.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst().orElse(null);
    }

    public List<Doctor> getAllDoctors() {
        return users.values().stream()
                .filter(u -> u instanceof Doctor)
                .map(u -> (Doctor) u)
                .collect(Collectors.toList());
    }

    public List<Patient> getAllPatients() {
        return users.values().stream()
                .filter(u -> u instanceof Patient)
                .map(u -> (Patient) u)
                .collect(Collectors.toList());
    }

    public List<Doctor> searchDoctors(String keyword, Specialization spec) {
        return getAllDoctors().stream()
                .filter(d -> {
                    boolean matchSpec = spec == null || d.getSpecialization() == spec;
                    boolean matchName = keyword == null || keyword.isBlank()
                            || d.getName().toLowerCase().contains(keyword.toLowerCase())
                            || d.getSpecialization().getName().toLowerCase().contains(keyword.toLowerCase());
                    return matchSpec && matchName;
                })
                .collect(Collectors.toList());
    }

    // ── Appointment operations ───────────────────────────────
    public void addAppointment(Appointment a)     { appointments.put(a.getAppointmentId(), a); }
    public Appointment getAppointmentById(String id){ return appointments.get(id); }
    public Collection<Appointment> getAllAppointments(){ return appointments.values(); }

    public List<Appointment> getAppointmentsForPatient(String patientId) {
        return appointments.values().stream()
                .filter(a -> a.getPatientId().equals(patientId))
                .sorted(Comparator.comparing(Appointment::getDate).reversed())
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsForDoctor(String doctorId) {
        return appointments.values().stream()
                .filter(a -> a.getDoctorId().equals(doctorId))
                .sorted(Comparator.comparing(Appointment::getDate))
                .collect(Collectors.toList());
    }

    public List<Appointment> getUpcomingAppointmentsForPatient(String patientId) {
        return getAppointmentsForPatient(patientId).stream()
                .filter(Appointment::isUpcoming)
                .filter(a -> a.getStatus() != AppointmentStatus.CANCELLED)
                .collect(Collectors.toList());
    }

    // ── Notification operations ──────────────────────────────
    public void addNotification(Notification n)   { notifications.add(n); }
    public List<Notification> getNotificationsForUser(String userId) {
        return notifications.stream()
                .filter(n -> n.getUserId().equals(userId))
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }
    public long countUnread(String userId) {
        return notifications.stream()
                .filter(n -> n.getUserId().equals(userId) && !n.isRead())
                .count();
    }

    // ── Seed sample data ────────────────────────────────────
    private void seedData() {
        // --- Admin ---
        Admin admin = new Admin("A001","Admin MediSphere","admin@medisphere.my",
                "admin123","0123456789","Management","SUPER_ADMIN");
        users.put(admin.getUserId(), admin);

        // --- Doctors ---
        Doctor d1 = new Doctor("D001","Dr. Ahmad Faisal","ahmad@medisphere.my","doc123",
                "0112345678", Specialization.CARDIOLOGY,"MBBS, Sp.JP",14,200.0);
        d1.setBiography("Experienced cardiologist specializing in coronary artery disease and heart failure management.");

        Doctor d2 = new Doctor("D002","Dr. Siti Nurhaliza","siti@medisphere.my","doc123",
                "0122345678", Specialization.NEUROLOGY,"MBBS, Sp.N",10,180.0);
        d2.setBiography("Board-certified neurologist focusing on stroke rehabilitation and epilepsy management.");

        Doctor d3 = new Doctor("D003","Dr. Rajesh Kumar","rajesh@medisphere.my","doc123",
                "0132345678", Specialization.ORTHOPEDICS,"MBBS, Sp.OT",8,150.0);
        d3.setBiography("Orthopaedic surgeon specializing in sports injuries and joint replacement procedures.");

        Doctor d4 = new Doctor("D004","Dr. Lee Wei Ming","lee@medisphere.my","doc123",
                "0142345678", Specialization.DERMATOLOGY,"MBBS, Sp.KK",6,130.0);
        d4.setBiography("Dermatologist with expertise in acne, eczema, and cosmetic skin treatments.");

        Doctor d5 = new Doctor("D005","Dr. Nurul Ain","nurul@medisphere.my","doc123",
                "0152345678", Specialization.PEDIATRICS,"MBBS, Sp.A",12,120.0);
        d5.setBiography("Dedicated paediatrician providing comprehensive healthcare for children from birth to adolescence.");

        Doctor d6 = new Doctor("D006","Dr. Kavitha Raj","kavitha@medisphere.my","doc123",
                "0162345678", Specialization.PSYCHIATRY,"MBBS, Sp.KJ",9,160.0);
        d6.setBiography("Psychiatrist with a holistic approach to mental health, focusing on anxiety, depression and trauma.");

        Doctor d7 = new Doctor("D007","Dr. Mohd Syafiq","syafiq@medisphere.my","doc123",
                "0172345678", Specialization.GENERAL_MEDICINE,"MBBS",5,80.0);
        d7.setBiography("General practitioner delivering family-centred primary care and preventive health services.");

        Doctor d8 = new Doctor("D008","Dr. Priya Shankar","priya@medisphere.my","doc123",
                "0182345678", Specialization.GYNECOLOGY,"MBBS, Sp.OG",11,170.0);
        d8.setBiography("Obstetrician & gynaecologist providing comprehensive women's health care.");

        for (Doctor d : new Doctor[]{d1,d2,d3,d4,d5,d6,d7,d8}) users.put(d.getUserId(), d);

        // --- Patient ---
        Patient p1 = new Patient("P001","Ali Hassan","ali@email.com","patient123",
                "0111234567","1990-05-15","O+");
        p1.setMedicalHistory("Mild hypertension diagnosed in 2022.");
        users.put(p1.getUserId(), p1);

        // --- Sample appointments ---
        Appointment a1 = new Appointment("APT001", "P001", "D001",
                LocalDate.now().plusDays(3), LocalTime.of(9,0), "Chest pain and shortness of breath");
        a1.setStatus(AppointmentStatus.CONFIRMED);
        appointments.put(a1.getAppointmentId(), a1);

        Appointment a2 = new Appointment("APT002", "P001", "D007",
                LocalDate.now().plusDays(7), LocalTime.of(10,0), "General checkup");
        a2.setStatus(AppointmentStatus.PENDING);
        appointments.put(a2.getAppointmentId(), a2);

        Appointment a3 = new Appointment("APT003", "P001", "D002",
                LocalDate.now().minusDays(10), LocalTime.of(14,0), "Recurring headaches");
        a3.setStatus(AppointmentStatus.COMPLETED);
        appointments.put(a3.getAppointmentId(), a3);

        p1.addAppointmentId("APT001");
        p1.addAppointmentId("APT002");
        p1.addAppointmentId("APT003");

        d1.addAppointmentId("APT001");
        d7.addAppointmentId("APT002");
        d2.addAppointmentId("APT003");
    }
}
