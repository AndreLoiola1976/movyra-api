package ai.movyra.application.service;

import ai.movyra.application.port.in.CreateBarberUseCase;
import ai.movyra.application.port.out.BarberRepository;
import ai.movyra.domain.model.Barber;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class BarberService implements CreateBarberUseCase {
    
    private final BarberRepository barberRepository;
    
    public BarberService(BarberRepository barberRepository) {
        this.barberRepository = barberRepository;
    }
    
    @Override
    @Transactional
    public Barber create(CreateBarberCommand command) {
        Barber barber = new Barber(
            UUID.randomUUID(),
            command.tenantId(),
            command.displayName()
        );
        
        if (command.phone() != null) {
            barber.setPhone(command.phone());
        }
        
        return barberRepository.save(barber);
    }
}
