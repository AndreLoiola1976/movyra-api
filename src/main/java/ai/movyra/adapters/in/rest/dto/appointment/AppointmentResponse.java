package ai.movyra.adapters.in.rest.dto.appointment;

import java.time.Instant;
import java.util.UUID;

public record AppointmentResponse(
    UUID id,
    UUID tenantId,
    UUID customerId,
    UUID professionalId,
    UUID serviceId,
    Instant startAt,
    Instant endAt,
    String status,
    int priceCents,
    String notes,
    Instant createdAt
) {}
