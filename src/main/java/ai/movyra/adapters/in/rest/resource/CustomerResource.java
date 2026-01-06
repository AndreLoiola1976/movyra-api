package ai.movyra.adapters.in.rest.resource;

import ai.movyra.adapters.in.rest.context.TenantContext;
import ai.movyra.adapters.in.rest.dto.CreateCustomerRequest;
import ai.movyra.adapters.in.rest.dto.CustomerResponse;
import ai.movyra.adapters.in.rest.mapper.CustomerMapper;
import ai.movyra.adapters.in.rest.tenant.TenantScoped;
import ai.movyra.application.port.in.customer.CreateCustomerUseCase;
import ai.movyra.domain.model.Customer;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/customers")
@Produces(MediaType.APPLICATION_JSON)
@TenantScoped
public class CustomerResource {

    @Inject
    TenantContext tenantContext;

    @Inject
    CreateCustomerUseCase createCustomerUseCase;

    @POST
    public Response create(CreateCustomerRequest request) {
        Customer customer = createCustomerUseCase.create(
                CustomerMapper.toCommand(tenantContext.getTenantId(), request)
        );
        CustomerResponse response = CustomerMapper.toResponse(customer);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}
