package ai.movyra.application.port.in.tenant;

import ai.movyra.domain.model.Tenant;

public interface UpdateTenantUseCase {
    Tenant update(UpdateTenantCommand command);
}
