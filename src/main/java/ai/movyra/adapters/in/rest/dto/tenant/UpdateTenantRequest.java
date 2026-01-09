package ai.movyra.adapters.in.rest.dto.tenant;

public record UpdateTenantRequest(
        String name,
        String phone,
        String timezone
) {}
