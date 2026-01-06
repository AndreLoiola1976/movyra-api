package ai.movyra.adapters.in.rest.dto;

public record UpdateTenantRequest(
        String name,
        String phone,
        String timezone
) {}
