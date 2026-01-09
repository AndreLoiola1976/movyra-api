package ai.movyra.adapters.in.rest.resource;

import ai.movyra.adapters.in.rest.dto.portfolio.CreatePortfolioItemRequest;
import ai.movyra.adapters.in.rest.dto.portfolio.PortfolioItemListResponse;
import ai.movyra.adapters.in.rest.dto.portfolio.PortfolioItemResponse;
import ai.movyra.adapters.in.rest.mapper.PortfolioItemMapper;
import ai.movyra.adapters.in.rest.tenant.TenantScoped;
import ai.movyra.application.port.in.portfolio.CreatePortfolioItemCommand;
import ai.movyra.application.port.in.portfolio.CreatePortfolioItemUseCase;
import ai.movyra.application.port.in.portfolio.ListPortfolioItemsUseCase;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/api/portfolio-items")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@TenantScoped
public class PortfolioItemResource {

    private final CreatePortfolioItemUseCase createUseCase;
    private final ListPortfolioItemsUseCase listUseCase;

    public PortfolioItemResource(CreatePortfolioItemUseCase createUseCase,
                                 ListPortfolioItemsUseCase listUseCase) {
        this.createUseCase = createUseCase;
        this.listUseCase = listUseCase;
    }

    @Operation(summary = "Create a portfolio item (tenant-scoped)")
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PortfolioItemResponse.class)
                    )
            ),
            @APIResponse(responseCode = "400", description = "Bad Request"),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "403", description = "Forbidden")
    })
    @POST
    public Response create(
            CreatePortfolioItemRequest req,
            @Parameter(description = "Tenant ID (UUID)", required = true)
            @HeaderParam("X-Tenant") UUID tenantId
    ) {
        if (tenantId == null) {
            throw new BadRequestException("X-Tenant header is required");
        }

        var created = createUseCase.execute(new CreatePortfolioItemCommand(
                req.title(),
                req.description(),
                req.imageUrl(),
                req.sortOrder() == null ? 0 : req.sortOrder()
        ));

        return Response.status(Response.Status.CREATED)
                .entity(PortfolioItemMapper.toResponse(created))
                .build();
    }

    @GET
    @Operation(summary = "List active portfolio items (tenant-scoped) -- TESTE-123")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PortfolioItemResponse.class)
                    )
            ),
            @APIResponse(responseCode = "400", description = "Bad Request"),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "403", description = "Forbidden")
    })
    public PortfolioItemListResponse listActive(
            @Parameter(description = "Tenant ID (UUID)", required = true)
            @HeaderParam("X-Tenant") UUID tenantId
    ) {
        if (tenantId == null) {
            throw new BadRequestException("X-Tenant header is required");
        }

        List<PortfolioItemResponse> items = listUseCase.listActiveOrdered()
                .stream()
                .map(PortfolioItemMapper::toResponse)
                .toList();

        return new PortfolioItemListResponse(items);
    }

}
