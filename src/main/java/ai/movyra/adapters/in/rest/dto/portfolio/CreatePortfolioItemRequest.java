package ai.movyra.adapters.in.rest.dto.portfolio;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CreatePortfolioItemRequest", description = "Payload to create a portfolio item (tenant-scoped)")
public record CreatePortfolioItemRequest(

        @Schema(
                description = "Title shown on the public site",
                required = true,
                example = "Classic Fade"
        )
        String title,

        @Schema(
                description = "Optional description shown on the public site",
                nullable = true,
                example = "Classic fade with clean finish."
        )
        String description,

        @Schema(
                description = "Optional image URL for the portfolio item. Blank will be treated as null.",
                nullable = true,
                example = "https://example.com/images/fade.jpg"
        )
        String imageUrl,

        @Schema(
                description = "Sort order (ascending). If omitted, defaults to 0.",
                nullable = true,
                example = "10"
        )
        Integer sortOrder
) {}
