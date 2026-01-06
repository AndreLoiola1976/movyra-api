package ai.movyra.application.port.in.tenant;

import java.util.UUID;

public interface DeactivateTenantUseCase {
    void deactivate(UUID id);
}

