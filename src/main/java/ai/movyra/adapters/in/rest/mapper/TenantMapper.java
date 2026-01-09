package ai.movyra.adapters.in.rest.mapper;

import ai.movyra.adapters.in.rest.dto.tenant.CreateTenantRequest;
import ai.movyra.adapters.in.rest.dto.tenant.TenantResponse;
import ai.movyra.application.port.in.tenant.CreateTenantUseCase;
import ai.movyra.domain.model.Tenant;
import ai.movyra.adapters.in.rest.dto.tenant.UpdateTenantRequest;
import ai.movyra.application.port.in.tenant.UpdateTenantCommand;
import java.util.UUID;

public class TenantMapper {
    
    public static CreateTenantUseCase.CreateTenantCommand toCommand(CreateTenantRequest request) {
        return new CreateTenantUseCase.CreateTenantCommand(
            request.slug(),
            request.name(),
            request.phone(),
            request.country(),
            request.timezone()
        );
    }
    
    public static TenantResponse toResponse(Tenant tenant) {
        return new TenantResponse(
            tenant.getId(),
            tenant.getSlug(),
            tenant.getName(),
            tenant.getPhone(),
            tenant.getCountry(),
            tenant.getTimezone(),
            tenant.isActive(),
            tenant.getCreatedAt()
        );
    }

    public static UpdateTenantCommand toUpdateCommand(UUID id, UpdateTenantRequest request) {
        return new UpdateTenantCommand(
                id,
                request.name(),
                request.phone(),
                request.timezone()
        );
    }
}
