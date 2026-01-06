package ai.movyra.application.port.in.customer;

import ai.movyra.domain.model.Customer;
import ai.movyra.domain.model.valueobject.TenantId;

public interface CreateCustomerUseCase {
    
    record CreateCustomerCommand(
        TenantId tenantId,
        String fullName,
        String phone,
        String email
    ) {}
    
    Customer create(CreateCustomerCommand command);
}
