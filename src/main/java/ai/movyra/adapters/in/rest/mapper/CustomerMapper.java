package ai.movyra.adapters.in.rest.mapper;

import ai.movyra.adapters.in.rest.dto.CreateCustomerRequest;
import ai.movyra.adapters.in.rest.dto.CustomerResponse;
import ai.movyra.application.port.in.CreateCustomerUseCase;
import ai.movyra.domain.model.Customer;
import ai.movyra.domain.model.valueobject.TenantId;

public class CustomerMapper {
    
    public static CreateCustomerUseCase.CreateCustomerCommand toCommand(TenantId tenantId, CreateCustomerRequest request) {
        return new CreateCustomerUseCase.CreateCustomerCommand(
            tenantId,
            request.fullName(),
            request.phone(),
            request.email()
        );
    }
    
    public static CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
            customer.getId(),
            customer.getTenantId().value(),
            customer.getFullName(),
            customer.getPhone(),
            customer.getEmail(),
            customer.getCreatedAt()
        );
    }
}
