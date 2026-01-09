package ai.movyra.adapters.in.rest.resource;

import ai.movyra.adapters.in.rest.context.TenantContext;
import ai.movyra.adapters.in.rest.dto.professional.ProfessionalResponse;
import ai.movyra.adapters.in.rest.dto.professional.CreateProfessionalRequest;
import ai.movyra.adapters.in.rest.mapper.ProfessionalMapper;
import ai.movyra.adapters.in.rest.tenant.TenantScoped;
import ai.movyra.application.port.in.professional.CreateProfessionalUseCase;
import ai.movyra.domain.model.Professional;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/professionals")
@Produces(MediaType.APPLICATION_JSON)
@TenantScoped
public class ProfessionalResource {

    @Inject
    TenantContext tenantContext;

    @Inject
    CreateProfessionalUseCase createProfessionalUseCase;

    @POST
    public Response create(CreateProfessionalRequest request) {
        Professional professional = createProfessionalUseCase.create(
                ProfessionalMapper.toCommand(tenantContext.getTenantId(), request)
        );
        ProfessionalResponse response = ProfessionalMapper.toResponse(professional);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}
