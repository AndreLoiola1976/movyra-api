package ai.movyra.adapters.out.persistence.mapper;

import ai.movyra.adapters.out.persistence.entity.TenantEntity;
import ai.movyra.domain.model.Tenant;

import java.time.Instant;

public class TenantEntityMapper {

    public TenantEntity toEntity(Tenant domain) {
        TenantEntity entity = new TenantEntity();
        entity.setId(domain.getId());
        entity.setSlug(domain.getSlug());
        entity.setName(domain.getName());
        entity.setPhone(domain.getPhone());
        entity.setCountry(domain.getCountry());
        entity.setTimezone(domain.getTimezone());
        entity.setActive(domain.isActive());

        // If domain already has createdAt, keep it; else entity will set on @PrePersist
        Instant createdAt = domain.getCreatedAt();
        if (createdAt != null) {
            entity.setCreatedAt(createdAt);
        }
        return entity;
    }

    public Tenant toDomain(TenantEntity entity) {
        Tenant tenant = new Tenant(
                entity.getId(),
                entity.getSlug(),
                entity.getName(),
                entity.getCountry(),
                entity.getTimezone()
        );

        tenant.setPhone(entity.getPhone());

        // assuming Tenant has these setters; if not, you must adjust the domain model accordingly
        tenant.setActive(entity.isActive());
        tenant.setCreatedAt(entity.getCreatedAt());

        return tenant;
    }
}
