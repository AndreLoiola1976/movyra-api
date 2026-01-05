package ai.movyra.adapters.in.rest.resource;

import ai.movyra.adapters.in.rest.context.TenantContext;
import ai.movyra.adapters.in.rest.dto.BarberResponse;
import ai.movyra.adapters.in.rest.dto.CreateBarberRequest;
import ai.movyra.adapters.in.rest.mapper.BarberMapper;
import ai.movyra.adapters.in.rest.tenant.TenantScoped;
import ai.movyra.application.port.in.CreateBarberUseCase;
import ai.movyra.domain.model.Barber;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/barbers")
@Produces(MediaType.APPLICATION_JSON)
@TenantScoped
public class BarberResource {

    @Inject
    TenantContext tenantContext;

    @Inject
    CreateBarberUseCase createBarberUseCase;

    @POST
    public Response create(CreateBarberRequest request) {
        Barber barber = createBarberUseCase.create(
                BarberMapper.toCommand(tenantContext.getTenantId(), request)
        );
        BarberResponse response = BarberMapper.toResponse(barber);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}
