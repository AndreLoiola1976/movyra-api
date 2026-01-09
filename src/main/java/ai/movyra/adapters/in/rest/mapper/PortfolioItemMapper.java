package ai.movyra.adapters.in.rest.mapper;

import ai.movyra.adapters.in.rest.dto.portfolio.PortfolioItemResponse;
import ai.movyra.domain.model.PortfolioItem;

public final class PortfolioItemMapper {

    private PortfolioItemMapper() {
    }

    public static PortfolioItemResponse toResponse(PortfolioItem item) {
        return new PortfolioItemResponse(
                item.getId(),          // PortfolioItemId → UUID
                item.getTitle(),
                item.getDescription(),
                item.getImageUrl(),            // pode ser null
                item.getSortOrder(),
                item.isActive(),
                item.getCreatedAt()
        );
    }
}
