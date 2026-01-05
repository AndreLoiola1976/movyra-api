package ai.movyra.application.port.out;

import ai.movyra.domain.model.Barber;
import ai.movyra.domain.model.valueobject.TenantId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BarberRepository {
    
    Barber save(Barber barber);
    
    Optional<Barber> findById(TenantId tenantId, UUID id);
    
    List<Barber> findAllByTenant(TenantId tenantId);
}
