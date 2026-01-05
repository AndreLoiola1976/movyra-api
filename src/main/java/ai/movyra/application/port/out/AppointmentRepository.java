package ai.movyra.application.port.out;

import ai.movyra.domain.model.Appointment;
import ai.movyra.domain.model.valueobject.TenantId;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository {
    
    Appointment save(Appointment appointment);
    
    Optional<Appointment> findById(TenantId tenantId, UUID id);
    
    List<Appointment> findByProfessionalAndTimeRange(TenantId tenantId, UUID professionalId, Instant start, Instant end);
    
    List<Appointment> findByTimeRange(TenantId tenantId, Instant start, Instant end);
}
