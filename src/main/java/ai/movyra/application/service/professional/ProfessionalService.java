package ai.movyra.application.service.professional;

import ai.movyra.application.port.in.professional.CreateProfessionalUseCase;
import ai.movyra.application.port.out.ProfessionalRepository;
import ai.movyra.domain.model.Professional;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class ProfessionalService implements CreateProfessionalUseCase {
    
    private final ProfessionalRepository professionalRepository;
    
    public ProfessionalService(ProfessionalRepository professionalRepository) {
        this.professionalRepository = professionalRepository;
    }
    
    @Override
    @Transactional
    public Professional create(CreateProfessionalCommand command) {
        Professional professional = new Professional(
            UUID.randomUUID(),
            command.tenantId(),
            command.displayName()
        );
        
        if (command.phone() != null) {
            professional.setPhone(command.phone());
        }
        
        return professionalRepository.save(professional);
    }
}
