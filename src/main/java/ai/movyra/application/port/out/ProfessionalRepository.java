package ai.movyra.application.port.out;

import ai.movyra.domain.model.Professional;
import ai.movyra.domain.model.valueobject.TenantId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfessionalRepository {
    
    Professional save(Professional professional);
    
    Optional<Professional> findById(TenantId tenantId, UUID id);
    
    List<Professional> findAllByTenant(TenantId tenantId);
}
