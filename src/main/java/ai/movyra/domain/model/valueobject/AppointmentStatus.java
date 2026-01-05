package ai.movyra.domain.model.valueobject;

public enum AppointmentStatus {
    REQUESTED,
    CONFIRMED,
    CANCELLED,
    COMPLETED,
    NO_SHOW;
    
    public static AppointmentStatus fromString(String value) {
        return valueOf(value.toUpperCase());
    }
    
    public String toDbValue() {
        return name().toLowerCase();
    }
}
