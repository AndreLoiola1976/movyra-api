package ai.movyra.application.port.in.portfolio;

public record CreatePortfolioItemCommand(
        String title,
        String description,
        String imageUrl,
        int sortOrder
) {}
