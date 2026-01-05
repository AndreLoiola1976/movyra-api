package ai.movyra.adapters.in.rest.mapper;

import ai.movyra.adapters.in.rest.dto.BarberResponse;
import ai.movyra.adapters.in.rest.dto.CreateBarberRequest;
import ai.movyra.application.port.in.CreateBarberUseCase;
import ai.movyra.domain.model.Barber;
import ai.movyra.domain.model.valueobject.TenantId;

public class BarberMapper {
    
    public static CreateBarberUseCase.CreateBarberCommand toCommand(TenantId tenantId, CreateBarberRequest request) {
        return new CreateBarberUseCase.CreateBarberCommand(
            tenantId,
            request.displayName(),
            request.phone()
        );
    }
    
    public static BarberResponse toResponse(Barber barber) {
        return new BarberResponse(
            barber.getId(),
            barber.getTenantId().value(),
            barber.getDisplayName(),
            barber.getPhone(),
            barber.isActive(),
            barber.getCreatedAt()
        );
    }
}
