package ai.movyra.application.port.in;

import ai.movyra.domain.model.Professional;
import ai.movyra.domain.model.valueobject.TenantId;

public interface CreateProfessionalUseCase {
    
    record CreateProfessionalCommand(
        TenantId tenantId,
        String displayName,
        String phone
    ) {}
    
    Professional create(CreateProfessionalCommand command);
}
