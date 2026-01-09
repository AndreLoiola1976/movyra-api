package ai.movyra.adapters.in.rest.dto.tenant;

public record CreateTenantRequest(
    String slug,
    String name,
    String phone,
    String country,
    String timezone
) {}
