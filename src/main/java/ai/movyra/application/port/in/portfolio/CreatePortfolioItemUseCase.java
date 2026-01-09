package ai.movyra.application.port.in.portfolio;

import ai.movyra.domain.model.PortfolioItem;

public interface CreatePortfolioItemUseCase {
    PortfolioItem execute(CreatePortfolioItemCommand command);
}
