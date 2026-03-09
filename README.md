# MediSphere – Smart Healthcare Appointment System
### Developed for: Software Design Proposal (Object-Oriented Java)

---

## 🚀 Quick Start

### Requirements
- Java JDK 8 or higher (`javac` and `java` commands)
- Windows 10 / macOS / Linux

### Run on Windows
```bat
run_windows.bat
```

### Run on Mac / Linux
```bash
bash run_unix.sh
```

### Manual Compilation
```bash
mkdir -p bin
find src -name "*.java" > sources.txt
javac -d bin -sourcepath src @sources.txt
java -cp bin medisphere.MediSphereApp
```

---

## 🔐 Demo Login Credentials

| Role    | Email                    | Password   |
|---------|--------------------------|------------|
| Patient | ali@email.com            | patient123 |
| Doctor  | ahmad@medisphere.my      | doc123     |
| Admin   | admin@medisphere.my      | admin123   |

---

## 🏗️ Design Patterns Applied

### 1. 🔒 Singleton (Creational)
**Class:** `medisphere.data.DataStore`
- Ensures only one central data repository exists throughout the app
- All collections (users, appointments, notifications) are stored here
- Accessed via `DataStore.getInstance()`

### 2. 🏭 Factory Method (Creational)
**Class:** `medisphere.patterns.factory.UserFactory`
- Creates `Patient`, `Doctor`, or `Admin` objects without exposing `new`
- Registers created users with the DataStore automatically
- Used during registration flow

### 3. 👁️ Observer (Behavioral)
**Classes:** `NotificationService` (Subject), `EmailObserver`, `SystemNotificationObserver`
- Appointment events (booked, cancelled, rescheduled) broadcast to all observers
- `EmailObserver` simulates email sending and saves email notifications
- `SystemNotificationObserver` creates in-app bell notifications
- Easily extensible with new observers (e.g., SMS) without changing existing code

### 4. 🧠 Strategy (Behavioral)
**Classes:** `RecommendationContext`, `SymptomBasedStrategy`, `HistoryBasedStrategy`
- Two interchangeable recommendation algorithms
- `SymptomBasedStrategy`: matches patient symptoms against specialization keywords
- `HistoryBasedStrategy`: ranks doctors by how often patient visited their specialization
- Strategy can be swapped at runtime without changing the context

### 5. 🏰 Facade (Structural)
**Class:** `medisphere.patterns.facade.AppointmentFacade`
- Single unified interface for all appointment operations
- Hides complexity of DataStore persistence + observer notifications + permission checks
- Methods: `bookAppointment()`, `cancelAppointment()`, `rescheduleAppointment()`, etc.

---

## 📐 OOP Principles

| Principle       | Where Applied                                                                 |
|-----------------|-------------------------------------------------------------------------------|
| **Encapsulation** | All model fields private with controlled getters/setters                    |
| **Inheritance**   | `Patient`, `Doctor`, `Admin` all extend abstract `User`                     |
| **Polymorphism**  | `getRole()`, `getRoleColor()` overridden in each user subclass               |
| **Abstraction**   | `User` abstract class, `RecommendationStrategy` and `AppointmentObserver` interfaces |

---

## 📁 Project Structure

```
MediSphere/
├── src/
│   └── medisphere/
│       ├── MediSphereApp.java          ← Entry point
│       ├── models/
│       │   ├── User.java               ← Abstract base class
│       │   ├── Patient.java            ← Patient subclass
│       │   ├── Doctor.java             ← Doctor subclass
│       │   ├── Admin.java              ← Admin subclass
│       │   ├── Appointment.java        ← Core entity
│       │   ├── Notification.java       ← Notification entity
│       │   ├── AppointmentStatus.java  ← Status enum
│       │   └── Specialization.java     ← Specialization enum
│       ├── data/
│       │   └── DataStore.java          ← SINGLETON pattern
│       ├── patterns/
│       │   ├── factory/
│       │   │   └── UserFactory.java    ← FACTORY METHOD pattern
│       │   ├── observer/
│       │   │   ├── AppointmentObserver.java
│       │   │   ├── NotificationService.java  ← OBSERVER subject
│       │   │   ├── EmailObserver.java         ← OBSERVER concrete A
│       │   │   └── SystemNotificationObserver.java ← OBSERVER concrete B
│       │   ├── strategy/
│       │   │   ├── RecommendationStrategy.java
│       │   │   ├── SymptomBasedStrategy.java  ← STRATEGY concrete A
│       │   │   ├── HistoryBasedStrategy.java  ← STRATEGY concrete B
│       │   │   └── RecommendationContext.java ← STRATEGY context
│       │   └── facade/
│       │       └── AppointmentFacade.java     ← FACADE pattern
│       └── ui/
│           ├── UITheme.java            ← Shared styling constants
│           ├── MainFrame.java          ← Main window with CardLayout
│           ├── LoginPanel.java         ← Split-screen login UI
│           ├── RegisterPanel.java      ← Registration form
│           ├── PatientDashboard.java   ← Patient portal
│           ├── DoctorDashboard.java    ← Doctor portal
│           └── AdminDashboard.java     ← Admin panel
├── run_windows.bat
├── run_unix.sh
└── README.md
```

---

## 🌟 Features by Role

### Patient
- Register/Login with account creation
- Search doctors by name or specialization
- View doctor profiles with ratings, qualifications, and consultation fees
- Book appointments with date/time/symptom selection
- Reschedule or cancel upcoming appointments
- Symptom-based specialist recommendation (Strategy Pattern)
- History-based specialist recommendation (Strategy Pattern)
- In-app and email notification center
- View appointment history with status tracking

### Doctor
- View today's and all upcoming appointments
- Mark appointments as completed with doctor notes
- View personal schedule
- Receive booking/cancellation notifications

### Admin
- System-wide statistics dashboard
- View and manage all appointments (cancel as needed)
- View and manage all users (toggle active status)
- System overview with key metrics

---

*MediSphere v1.0 | Built with Java Swing | 5 Design Patterns | Full OOP*
