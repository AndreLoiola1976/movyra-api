package ai.movyra.adapters.in.rest.dto;

public record CreateTenantRequest(
    String slug,
    String name,
    String phone,
    String country,
    String timezone
) {}
