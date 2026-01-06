package ai.movyra.application.port.out;

import ai.movyra.domain.model.Tenant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantRepository {
    
    Tenant save(Tenant tenant);
    
    Optional<Tenant> findById(UUID id);
    
    Optional<Tenant> findBySlug(String slug);

    List<Tenant> findAllActive();

    void deactivate(UUID id);

    List<Tenant> findActivePage(int offset, int limit, String sortField, boolean sortDesc);

    long countActive();
}
