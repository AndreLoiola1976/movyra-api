package ai.movyra.adapters.in.rest.dto;

import java.time.Instant;
import java.util.UUID;

public record CustomerResponse(
    UUID id,
    UUID tenantId,
    String fullName,
    String phone,
    String email,
    Instant createdAt
) {}
