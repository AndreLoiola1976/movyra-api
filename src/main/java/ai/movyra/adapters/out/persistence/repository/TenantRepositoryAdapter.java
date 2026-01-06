package ai.movyra.adapters.out.persistence.repository;

import ai.movyra.adapters.out.persistence.entity.TenantEntity;
import ai.movyra.adapters.out.persistence.mapper.TenantEntityMapper;
import ai.movyra.application.port.out.TenantRepository;
import ai.movyra.domain.model.Tenant;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TenantRepositoryAdapter implements TenantRepository {

    @PersistenceContext
    EntityManager em;

    private final TenantEntityMapper mapper;

    public TenantRepositoryAdapter(TenantEntityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Tenant save(Tenant tenant) {
        TenantEntity entity = mapper.toEntity(tenant);

        TenantEntity managed = em.find(TenantEntity.class, entity.getId());
        if (managed == null) {
            em.persist(entity);
            return mapper.toDomain(entity);
        }

        TenantEntity merged = em.merge(entity);
        return mapper.toDomain(merged);
    }

    @Override
    public Optional<Tenant> findById(UUID id) {
        if (id == null) return Optional.empty();
        TenantEntity entity = em.find(TenantEntity.class, id);
        return Optional.ofNullable(entity).map(mapper::toDomain);
    }

    @Override
    public Optional<Tenant> findBySlug(String slug) {
        if (slug == null || slug.isBlank()) return Optional.empty();

        String normalized = slug.trim().toLowerCase();

        return em.createQuery(
                        "select t from TenantEntity t where t.slug = :slug",
                        TenantEntity.class
                )
                .setParameter("slug", normalized)
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .map(mapper::toDomain);
    }

    @Override
    public List<Tenant> findAllActive() {
        // Ajuste o nome do campo: "active" vs "isActive"
        return em.createQuery(
                        "select t from TenantEntity t where t.active = true",
                        TenantEntity.class
                )
                .getResultList()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
