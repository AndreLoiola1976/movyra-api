package ai.movyra.adapters.out.persistence.mapper;

import ai.movyra.adapters.out.persistence.entity.BarberEntity;
import ai.movyra.domain.model.Barber;
import ai.movyra.domain.model.valueobject.TenantId;

public class BarberEntityMapper {
    
    public BarberEntity toEntity(Barber domain) {
        BarberEntity entity = new BarberEntity();
        entity.setId(domain.getId());
        entity.setTenantId(domain.getTenantId().value());
        entity.setDisplayName(domain.getDisplayName());
        entity.setPhone(domain.getPhone());
        entity.setActive(domain.isActive());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }
    
    public Barber toDomain(BarberEntity entity) {
        Barber barber = new Barber(
            entity.getId(),
            TenantId.of(entity.getTenantId()),
            entity.getDisplayName()
        );
        barber.setPhone(entity.getPhone());
        barber.setActive(entity.isActive());
        return barber;
    }
}
