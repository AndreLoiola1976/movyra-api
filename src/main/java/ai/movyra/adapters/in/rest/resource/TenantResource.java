package ai.movyra.adapters.in.rest.resource;

import ai.movyra.adapters.in.rest.dto.CreateTenantRequest;
import ai.movyra.adapters.in.rest.dto.TenantResponse;
import ai.movyra.adapters.in.rest.dto.UpdateTenantRequest;
import ai.movyra.adapters.in.rest.mapper.TenantMapper;
import ai.movyra.application.port.in.tenant.*;

import ai.movyra.domain.model.Tenant;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

import ai.movyra.adapters.in.rest.dto.PagedResponse;

@Path("/api/tenants")
@Produces(MediaType.APPLICATION_JSON)
public class TenantResource {
    
    @Inject
    CreateTenantUseCase createTenantUseCase;

    @Inject
    GetTenantUseCase getTenantUseCase;

    @Inject
    ListTenantUseCase listTenantUseCase;

    @Inject
    DeactivateTenantUseCase deactivateTenantUseCase;

    @Inject
    UpdateTenantUseCase updateTenantUseCase;

    @POST
    public Response create(CreateTenantRequest request) {
        Tenant tenant = createTenantUseCase.create(TenantMapper.toCommand(request));
        TenantResponse response = TenantMapper.toResponse(tenant);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Path("/{id}")
    public TenantResponse getById(@PathParam("id") UUID id) {
        Tenant tenant = getTenantUseCase.getById(id);
        return TenantMapper.toResponse(tenant);
    }

    @GET
    public PagedResponse<TenantResponse> listAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size,
            @QueryParam("sort") @DefaultValue("createdAt") String sort,
            @QueryParam("desc") @DefaultValue("true") boolean desc
    ) {
        long total = listTenantUseCase.countActiveTenants();

        List<TenantResponse> items = listTenantUseCase.listActivePage(page, size, sort, desc)
                .stream()
                .map(TenantMapper::toResponse)
                .toList();

        String safeSort = switch (sort) {
            case "createdAt", "name", "slug" -> sort;
            default -> "createdAt";
        };

        String sortExpr = safeSort + "," + (desc ? "desc" : "asc");
        return new PagedResponse<>(items, page, size, total, sortExpr);
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public TenantResponse update(@PathParam("id") UUID id, UpdateTenantRequest request) {
        Tenant updated = updateTenantUseCase.update(TenantMapper.toUpdateCommand(id, request));
        return TenantMapper.toResponse(updated);
    }

    @POST
    @Path("/{id}/deactivate")
    public Response deactivate(@PathParam("id") UUID id) {
        deactivateTenantUseCase.deactivate(id);
        return Response.noContent().build();
    }
}
