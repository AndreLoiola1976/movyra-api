package ai.movyra.application.port.in;

import ai.movyra.domain.model.Barber;
import ai.movyra.domain.model.valueobject.TenantId;

public interface CreateBarberUseCase {
    
    record CreateBarberCommand(
        TenantId tenantId,
        String displayName,
        String phone
    ) {}
    
    Barber create(CreateBarberCommand command);
}
