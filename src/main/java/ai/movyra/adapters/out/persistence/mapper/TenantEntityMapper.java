package ai.movyra.adapters.out.persistence.mapper;

import ai.movyra.adapters.out.persistence.entity.TenantEntity;
import ai.movyra.domain.model.Tenant;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.Objects;

@ApplicationScoped
public class TenantEntityMapper {

    public TenantEntity toEntity(Tenant domain) {
        Objects.requireNonNull(domain, "domain tenant must not be null");

        TenantEntity entity = new TenantEntity();
        entity.setId(domain.getId());
        entity.setSlug(domain.getSlug());
        entity.setName(domain.getName());
        entity.setPhone(domain.getPhone());
        entity.setAddressLine1(domain.getAddressLine1());
        entity.setAddressLine2(domain.getAddressLine2());
        entity.setCity(domain.getCity());
        entity.setState(domain.getState());
        entity.setZip(domain.getZip());

        entity.setBillingEmail(domain.getBillingEmail());
        entity.setBillingName(domain.getBillingName());
        entity.setCountry(domain.getCountry());
        entity.setTimezone(domain.getTimezone());
        entity.setBillingStatus(domain.getBillingStatus());
        entity.setActive(domain.isActive());
        //entity.setLogoUrl(domain.getLogoUrl());

        // Keep createdAt if domain already has it; otherwise rely on @PrePersist in entity.
        Instant createdAt = domain.getCreatedAt();
        if (createdAt != null) {
            entity.setCreatedAt(createdAt);
        }

        return entity;
    }

    public Tenant toDomain(TenantEntity entity) {
        Objects.requireNonNull(entity, "tenant entity must not be null");

        Tenant tenant = new Tenant(
                entity.getId(),
                entity.getSlug(),
                entity.getName(),
                entity.getCountry(),
                entity.getTimezone()
        );

        // Optional fields
        tenant.setPhone(entity.getPhone());
        tenant.setAddressLine1(entity.getAddressLine1());
        tenant.setAddressLine2(entity.getAddressLine2());
        tenant.setCity(entity.getCity());
        tenant.setState(entity.getState());
        tenant.setZip(entity.getZip());

        tenant.setBillingEmail(entity.getBillingEmail());
        tenant.setBillingName(entity.getBillingName());

        tenant.setBillingStatus(entity.getBillingStatus());

        // These setters must exist in your domain model
        tenant.setActive(entity.isActive());
        tenant.setCreatedAt(entity.getCreatedAt());

        return tenant;
    }
}
