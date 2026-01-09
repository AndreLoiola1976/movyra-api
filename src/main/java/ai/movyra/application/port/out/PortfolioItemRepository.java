package ai.movyra.application.port.out;

import ai.movyra.domain.model.PortfolioItem;
import ai.movyra.domain.model.valueobject.TenantId;

import java.util.List;

public interface PortfolioItemRepository {

    void save(PortfolioItem item);

    List<PortfolioItem> findActiveByTenant(TenantId tenantId);
}
