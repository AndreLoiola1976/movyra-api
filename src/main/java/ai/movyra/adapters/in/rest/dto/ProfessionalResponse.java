package ai.movyra.adapters.in.rest.dto;

import java.time.Instant;
import java.util.UUID;

public record ProfessionalResponse(
    UUID id,
    UUID tenantId,
    String displayName,
    String phone,
    boolean active,
    Instant createdAt
) {}
