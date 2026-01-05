package ai.movyra.adapters.out.persistence.repository;

import ai.movyra.adapters.out.persistence.entity.BarberEntity;
import ai.movyra.adapters.out.persistence.mapper.BarberEntityMapper;
import ai.movyra.application.port.out.BarberRepository;
import ai.movyra.domain.model.Barber;
import ai.movyra.domain.model.valueobject.TenantId;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class BarberPanacheRepository implements PanacheRepositoryBase<BarberEntity, UUID>, BarberRepository {
    
    private final BarberEntityMapper mapper = new BarberEntityMapper();
    
    @Override
    public Barber save(Barber barber) {
        BarberEntity entity = mapper.toEntity(barber);
        persist(entity);
        return mapper.toDomain(entity);
    }
    
    @Override
    public Optional<Barber> findById(TenantId tenantId, UUID id) {
        return find("tenantId = ?1 and id = ?2", tenantId.value(), id)
            .firstResultOptional()
            .map(mapper::toDomain);
    }
    
    @Override
    public List<Barber> findAllByTenant(TenantId tenantId) {
        return find("tenantId", tenantId.value())
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
}
