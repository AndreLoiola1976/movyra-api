package ai.movyra.application.port.in.portfolio;

import ai.movyra.domain.model.PortfolioItem;
import java.util.List;

public interface ListPortfolioItemsUseCase {
    List<PortfolioItem> listActiveOrdered();
}
