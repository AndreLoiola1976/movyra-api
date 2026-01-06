package ai.movyra.application.port.in.appointment;

import ai.movyra.domain.model.Appointment;
import ai.movyra.domain.model.valueobject.TenantId;

import java.time.Instant;
import java.util.UUID;

public interface CreateAppointmentUseCase {
    
    record CreateAppointmentCommand(
        TenantId tenantId,
        UUID customerId,
        UUID professionalId,
        UUID serviceId,
        Instant startAt,
        Instant endAt,
        int priceCents,
        String notes
    ) {}
    
    Appointment create(CreateAppointmentCommand command);
}
