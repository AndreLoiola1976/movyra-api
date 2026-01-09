package ai.movyra.adapters.out.persistence.repository;

import ai.movyra.adapters.out.persistence.entity.PortfolioItemEntity;
import ai.movyra.adapters.out.persistence.mapper.PortfolioItemEntityMapper;
import ai.movyra.application.port.out.PortfolioItemRepository;
import ai.movyra.domain.model.PortfolioItem;
import ai.movyra.domain.model.valueobject.TenantId;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PortfolioItemRepositoryAdapter implements PortfolioItemRepository {

    private final PortfolioItemPanacheRepository panache;

    public PortfolioItemRepositoryAdapter(PortfolioItemPanacheRepository panache) {
        this.panache = panache;
    }

    @Override
    public void save(PortfolioItem item) {
        PortfolioItemEntity e = PortfolioItemEntityMapper.toEntity(item);
        panache.persistAndFlush(e);
    }

    @Override
    public List<PortfolioItem> findActiveByTenant(TenantId tenantId) {
        return panache.find(
                        "tenantId = ?1 and active = ?2 order by sortOrder asc, createdAt desc",
                        tenantId.value(),
                        true
                )
                .stream()
                .map(PortfolioItemEntityMapper::toDomain)
                .toList();
    }
}
