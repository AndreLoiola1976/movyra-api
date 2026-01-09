package ai.movyra.domain.model.valueobject;

import java.util.Objects;
import java.util.UUID;

public record PortfolioItemId(UUID value) {

    public PortfolioItemId {
        Objects.requireNonNull(value, "portfolioItemId must not be null");
    }

    public static PortfolioItemId newId() {
        return new PortfolioItemId(UUID.randomUUID());
    }

    public static PortfolioItemId of(UUID value) {
        return new PortfolioItemId(value);
    }
}
