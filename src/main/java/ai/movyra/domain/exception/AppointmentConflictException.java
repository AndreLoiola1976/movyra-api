package ai.movyra.domain.exception;

import java.util.UUID;

public class AppointmentConflictException extends DomainException {
    
    private final UUID barberId;
    private final String timeRange;
    
    public AppointmentConflictException(UUID barberId, String timeRange) {
        super(String.format("Barber %s already has an appointment during %s", barberId, timeRange));
        this.barberId = barberId;
        this.timeRange = timeRange;
    }
    
    public UUID getBarberId() {
        return barberId;
    }
    
    public String getTimeRange() {
        return timeRange;
    }
}
