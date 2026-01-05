package ai.movyra.adapters.out.persistence.mapper;

import ai.movyra.adapters.out.persistence.entity.CustomerEntity;
import ai.movyra.domain.model.Customer;
import ai.movyra.domain.model.valueobject.TenantId;

public class CustomerEntityMapper {
    
    public CustomerEntity toEntity(Customer domain) {
        CustomerEntity entity = new CustomerEntity();
        entity.setId(domain.getId());
        entity.setTenantId(domain.getTenantId().value());
        entity.setFullName(domain.getFullName());
        entity.setPhone(domain.getPhone());
        entity.setEmail(domain.getEmail());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }
    
    public Customer toDomain(CustomerEntity entity) {
        Customer customer = new Customer(
            entity.getId(),
            TenantId.of(entity.getTenantId()),
            entity.getFullName()
        );
        customer.setPhone(entity.getPhone());
        customer.setEmail(entity.getEmail());
        return customer;
    }
}
