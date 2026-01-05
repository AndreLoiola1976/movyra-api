package ai.movyra.domain.exception;

import java.util.UUID;

public class AppointmentConflictException extends DomainException {
    
    private final UUID professionalId;
    private final String timeRange;
    
    public AppointmentConflictException(UUID professionalId, String timeRange) {
        super(String.format("Professional %s already has an appointment during %s", professionalId, timeRange));
        this.professionalId = professionalId;
        this.timeRange = timeRange;
    }
    
    public UUID getProfessionalId() {
        return professionalId;
    }
    
    public String getTimeRange() {
        return timeRange;
    }
}
