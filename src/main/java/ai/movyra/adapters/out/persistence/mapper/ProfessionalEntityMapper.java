package ai.movyra.adapters.out.persistence.mapper;

import ai.movyra.adapters.out.persistence.entity.ProfessionalEntity;
import ai.movyra.domain.model.Professional;
import ai.movyra.domain.model.valueobject.TenantId;

public class ProfessionalEntityMapper {
    
    public ProfessionalEntity toEntity(Professional domain) {
        ProfessionalEntity entity = new ProfessionalEntity();
        entity.setId(domain.getId());
        entity.setTenantId(domain.getTenantId().value());
        entity.setDisplayName(domain.getDisplayName());
        entity.setPhone(domain.getPhone());
        entity.setActive(domain.isActive());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }
    
    public Professional toDomain(ProfessionalEntity entity) {
        Professional professional = new Professional(
            entity.getId(),
            TenantId.of(entity.getTenantId()),
            entity.getDisplayName()
        );
        professional.setPhone(entity.getPhone());
        professional.setActive(entity.isActive());
        return professional;
    }
}
