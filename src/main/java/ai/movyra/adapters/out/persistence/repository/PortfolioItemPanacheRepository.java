package ai.movyra.adapters.out.persistence.repository;

import ai.movyra.adapters.out.persistence.entity.PortfolioItemEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class PortfolioItemPanacheRepository implements PanacheRepositoryBase<PortfolioItemEntity, UUID> {
}
