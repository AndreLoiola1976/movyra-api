package ai.movyra.application.service.portfolio;

import ai.movyra.adapters.in.rest.context.TenantContext;
import ai.movyra.application.port.in.portfolio.CreatePortfolioItemCommand;
import ai.movyra.application.port.in.portfolio.CreatePortfolioItemUseCase;
import ai.movyra.application.port.in.portfolio.ListPortfolioItemsUseCase;
import ai.movyra.application.port.out.PortfolioItemRepository;
import ai.movyra.domain.model.PortfolioItem;
import ai.movyra.domain.model.valueobject.TenantId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class PortfolioItemService implements CreatePortfolioItemUseCase, ListPortfolioItemsUseCase {

    private final PortfolioItemRepository repository;
    private final TenantContext tenantContext;

    public PortfolioItemService(
            PortfolioItemRepository repository,
            TenantContext tenantContext
    ) {
        this.repository = repository;
        this.tenantContext = tenantContext;
    }

    @Transactional
    @Override
    public PortfolioItem execute(CreatePortfolioItemCommand cmd) {
        TenantId tenantId = tenantContext.getTenantId();

        PortfolioItem item = new PortfolioItem(tenantId.value(), cmd.title());
        item.setDescription(cmd.description());
        item.setImageUrl(cmd.imageUrl());     // optional, blank->null
        item.setSortOrder(cmd.sortOrder());

        repository.save(item);
        return item;
    }

    @Override
    public List<PortfolioItem> listActiveOrdered() {
        TenantId tenantId = tenantContext.getTenantId();
        return repository.findActiveByTenant(tenantId);
    }
}
