package ai.movyra.adapters.in.rest.mapper;

import ai.movyra.adapters.in.rest.dto.CreateTenantRequest;
import ai.movyra.adapters.in.rest.dto.TenantResponse;
import ai.movyra.application.port.in.CreateTenantUseCase;
import ai.movyra.domain.model.Tenant;

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
}
