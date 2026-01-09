package ai.movyra.adapters.out.persistence.mapper;

import ai.movyra.adapters.out.persistence.entity.PortfolioItemEntity;
import ai.movyra.domain.model.PortfolioItem;

import java.util.Objects;

public final class PortfolioItemEntityMapper {

    private PortfolioItemEntityMapper() {}

    // =========================
    // Domain -> Entity
    // =========================
    public static PortfolioItemEntity toEntity(PortfolioItem domain) {
        if (domain == null) return null;

        PortfolioItemEntity e = new PortfolioItemEntity();
        e.setId(domain.getId());
        e.setTenantId(domain.getTenantIdValue());
        e.setTitle(domain.getTitle());
        e.setDescription(domain.getDescription());
        e.setImageUrl(domain.getImageUrl()); // optional
        e.setSortOrder(domain.getSortOrder() != null ? domain.getSortOrder() : 0);
        e.setActive(domain.isActive());

        // persistence truth
        e.setCreatedAt(Objects.requireNonNull(domain.getCreatedAt(), "createdAt cannot be null"));

        // updatedAt not in domain yet -> leave null
        return e;
    }

    // =========================
    // Entity -> Domain
    // =========================
    public static PortfolioItem toDomain(PortfolioItemEntity entity) {
        if (entity == null) return null;

        // Build a valid instance, then restore persistence truth via setters (Tenant-style).
        PortfolioItem d = new PortfolioItem(entity.getTenantId(), entity.getTitle());

        d.setId(require(entity.getId(), "id"));
        d.setTenantId(require(entity.getTenantId(), "tenantId"));

        d.setDescription(entity.getDescription());
        d.setImageUrl(entity.getImageUrl()); // blank->null handled by domain setter
        d.setSortOrder(entity.getSortOrder());

        d.setActive(entity.isActive());
        d.setCreatedAt(require(entity.getCreatedAt(), "createdAt"));

        return d;
    }

    private static <T> T require(T value, String field) {
        return Objects.requireNonNull(value, field + " cannot be null");
    }
}
