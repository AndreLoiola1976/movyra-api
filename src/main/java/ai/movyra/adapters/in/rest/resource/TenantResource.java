package ai.movyra.adapters.in.rest.resource;

import ai.movyra.adapters.in.rest.dto.CreateTenantRequest;
import ai.movyra.adapters.in.rest.dto.TenantResponse;
import ai.movyra.adapters.in.rest.mapper.TenantMapper;
import ai.movyra.application.port.in.CreateTenantUseCase;
import ai.movyra.domain.model.Tenant;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/tenants")
@Produces(MediaType.APPLICATION_JSON)
public class TenantResource {
    
    @Inject
    CreateTenantUseCase createTenantUseCase;
    
    @POST
    public Response create(CreateTenantRequest request) {
        Tenant tenant = createTenantUseCase.create(TenantMapper.toCommand(request));
        TenantResponse response = TenantMapper.toResponse(tenant);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}
