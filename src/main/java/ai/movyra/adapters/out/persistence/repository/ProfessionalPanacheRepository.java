package ai.movyra.adapters.out.persistence.repository;

import ai.movyra.adapters.out.persistence.entity.ProfessionalEntity;
import ai.movyra.adapters.out.persistence.mapper.ProfessionalEntityMapper;
import ai.movyra.application.port.out.ProfessionalRepository;
import ai.movyra.domain.model.Professional;
import ai.movyra.domain.model.valueobject.TenantId;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ProfessionalPanacheRepository implements PanacheRepositoryBase<ProfessionalEntity, UUID>, ProfessionalRepository {
    
    private final ProfessionalEntityMapper mapper = new ProfessionalEntityMapper();
    
    @Override
    public Professional save(Professional professional) {
        ProfessionalEntity entity = mapper.toEntity(professional);
        persist(entity);
        return mapper.toDomain(entity);
    }
    
    @Override
    public Optional<Professional> findById(TenantId tenantId, UUID id) {
        return find("tenantId = ?1 and id = ?2", tenantId.value(), id)
            .firstResultOptional()
            .map(mapper::toDomain);
    }
    
    @Override
    public List<Professional> findAllByTenant(TenantId tenantId) {
        return find("tenantId", tenantId.value())
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
}
