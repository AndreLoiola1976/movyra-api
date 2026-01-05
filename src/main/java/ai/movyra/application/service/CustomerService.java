package ai.movyra.application.service;

import ai.movyra.application.port.in.CreateCustomerUseCase;
import ai.movyra.application.port.out.CustomerRepository;
import ai.movyra.domain.model.Customer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class CustomerService implements CreateCustomerUseCase {
    
    private final CustomerRepository customerRepository;
    
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    @Override
    @Transactional
    public Customer create(CreateCustomerCommand command) {
        Customer customer = new Customer(
            UUID.randomUUID(),
            command.tenantId(),
            command.fullName()
        );
        
        if (command.phone() != null) {
            customer.setPhone(command.phone());
        }
        
        if (command.email() != null) {
            customer.setEmail(command.email());
        }
        
        return customerRepository.save(customer);
    }
}
