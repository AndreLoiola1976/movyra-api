package ai.movyra.adapters.in.rest.resource;

import ai.movyra.adapters.in.rest.dto.CreateTenantRequest;
import ai.movyra.adapters.in.rest.dto.TenantResponse;
import ai.movyra.adapters.in.rest.mapper.TenantMapper;
import ai.movyra.application.port.in.tenant.CreateTenantUseCase;
import ai.movyra.application.port.in.tenant.GetTenantUseCase;
import ai.movyra.application.port.in.tenant.ListTenantUseCase;
import ai.movyra.application.port.in.tenant.DeactivateTenantUseCase;

import ai.movyra.domain.model.Tenant;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

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
    public List<TenantResponse> listAll() {
        return listTenantUseCase.listAllTenants()
                .stream()
                .map(TenantMapper::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @POST
    @Path("/{id}/deactivate")
    public Response deactivate(@PathParam("id") UUID id) {
        deactivateTenantUseCase.deactivate(id);
        return Response.noContent().build();
    }
}
