package ai.movyra.adapters.out.persistence.repository;

import ai.movyra.adapters.out.persistence.entity.CustomerEntity;
import ai.movyra.adapters.out.persistence.mapper.CustomerEntityMapper;
import ai.movyra.application.port.out.CustomerRepository;
import ai.movyra.domain.model.Customer;
import ai.movyra.domain.model.valueobject.TenantId;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CustomerPanacheRepository implements PanacheRepositoryBase<CustomerEntity, UUID>, CustomerRepository {
    
    private final CustomerEntityMapper mapper = new CustomerEntityMapper();
    
    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = mapper.toEntity(customer);
        persist(entity);
        return mapper.toDomain(entity);
    }
    
    @Override
    public Optional<Customer> findById(TenantId tenantId, UUID id) {
        return find("tenantId = ?1 and id = ?2", tenantId.value(), id)
            .firstResultOptional()
            .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Customer> findByPhone(TenantId tenantId, String phone) {
        return find("tenantId = ?1 and phone = ?2", tenantId.value(), phone)
            .firstResultOptional()
            .map(mapper::toDomain);
    }
    
    @Override
    public List<Customer> findAllByTenant(TenantId tenantId) {
        return find("tenantId", tenantId.value())
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
}
