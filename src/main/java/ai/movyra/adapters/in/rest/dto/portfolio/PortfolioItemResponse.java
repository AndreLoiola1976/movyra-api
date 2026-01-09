package ai.movyra.adapters.in.rest.dto.portfolio;

import java.time.Instant;
import java.util.UUID;

public record PortfolioItemResponse(
        UUID id,
        String title,
        String description,
        String imageUrl,
        int sortOrder,
        boolean active,
        Instant createdAt
) {}
