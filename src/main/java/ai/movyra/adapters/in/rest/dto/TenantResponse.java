package ai.movyra.adapters.in.rest.dto;

import java.time.Instant;
import java.util.UUID;

public record TenantResponse(
    UUID id,
    String slug,
    String name,
    String phone,
    String country,
    String timezone,
    boolean active,
    Instant createdAt
) {}
