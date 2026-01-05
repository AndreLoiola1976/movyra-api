package ai.movyra.application.port.in;

import ai.movyra.domain.model.Tenant;

public interface CreateTenantUseCase {

    record CreateTenantCommand(
            String slug,
            String name,
            String phone,
            String country,
            String timezone
    ) { }

    Tenant create(CreateTenantCommand command);
}
