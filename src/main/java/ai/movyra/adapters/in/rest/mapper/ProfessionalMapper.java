package ai.movyra.adapters.in.rest.mapper;

import ai.movyra.adapters.in.rest.dto.ProfessionalResponse;
import ai.movyra.adapters.in.rest.dto.CreateProfessionalRequest;
import ai.movyra.application.port.in.CreateProfessionalUseCase;
import ai.movyra.domain.model.Professional;
import ai.movyra.domain.model.valueobject.TenantId;

public class ProfessionalMapper {
    
    public static CreateProfessionalUseCase.CreateProfessionalCommand toCommand(TenantId tenantId, CreateProfessionalRequest request) {
        return new CreateProfessionalUseCase.CreateProfessionalCommand(
            tenantId,
            request.displayName(),
            request.phone()
        );
    }
    
    public static ProfessionalResponse toResponse(Professional professional) {
        return new ProfessionalResponse(
            professional.getId(),
            professional.getTenantId().value(),
            professional.getDisplayName(),
            professional.getPhone(),
            professional.isActive(),
            professional.getCreatedAt()
        );
    }
}
