package ai.movyra.application.port.out;

import ai.movyra.domain.model.Customer;
import ai.movyra.domain.model.valueobject.TenantId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    
    Customer save(Customer customer);
    
    Optional<Customer> findById(TenantId tenantId, UUID id);
    
    Optional<Customer> findByPhone(TenantId tenantId, String phone);
    
    List<Customer> findAllByTenant(TenantId tenantId);
}
