package ai.movyra.application.port.in.tenant;

import java.util.UUID;

public record UpdateTenantCommand(
        UUID tenantId,
        String name,
        String phone,
        String timezone
) {}

