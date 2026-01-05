package ai.movyra.adapters.in.rest.dto;

import java.time.Instant;
import java.util.UUID;

public record CreateAppointmentRequest(
    UUID customerId,
    UUID barberId,
    UUID serviceId,
    Instant startAt,
    Instant endAt,
    int priceCents,
    String notes
) {}
