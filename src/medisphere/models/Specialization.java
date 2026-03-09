package medisphere.models;

public enum Specialization {
    CARDIOLOGY("Cardiology", "Heart & Cardiovascular System", new String[]{"chest pain","heart palpitations","shortness of breath","hypertension"}),
    NEUROLOGY("Neurology", "Brain & Nervous System", new String[]{"headache","dizziness","memory loss","seizure","numbness"}),
    ORTHOPEDICS("Orthopedics", "Bones, Joints & Muscles", new String[]{"joint pain","back pain","fracture","muscle pain","arthritis"}),
    PEDIATRICS("Pediatrics", "Children's Health (0-18 yrs)", new String[]{"fever","child growth","vaccination","rash","ear pain"}),
    DERMATOLOGY("Dermatology", "Skin, Hair & Nails", new String[]{"rash","acne","skin allergy","eczema","hair loss"}),
    OPHTHALMOLOGY("Ophthalmology", "Eyes & Vision", new String[]{"blurry vision","eye pain","red eye","vision loss","eye infection"}),
    PSYCHIATRY("Psychiatry", "Mental Health & Wellness", new String[]{"depression","anxiety","insomnia","stress","mood swings"}),
    GENERAL_MEDICINE("General Medicine", "General & Family Health", new String[]{"fever","cold","cough","fatigue","body ache","flu"}),
    GYNECOLOGY("Gynecology", "Women's Health", new String[]{"menstrual pain","pregnancy","pelvic pain","hormonal issues"}),
    ONCOLOGY("Oncology", "Cancer Screening & Treatment", new String[]{"lump","unexplained weight loss","fatigue","cancer screening"});

    private final String name;
    private final String description;
    private final String[] keywords;

    Specialization(String name, String description, String[] keywords) {
        this.name = name;
        this.description = description;
        this.keywords = keywords;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String[] getKeywords() { return keywords; }

    @Override
    public String toString() { return name; }
}
