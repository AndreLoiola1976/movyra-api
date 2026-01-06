package ai.movyra.application.port.in.tenant;

import ai.movyra.domain.model.Tenant;

import java.util.UUID;

public interface GetTenantUseCase {

    Tenant getById(UUID tenantId);

}
