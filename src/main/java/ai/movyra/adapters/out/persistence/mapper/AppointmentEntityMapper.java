package ai.movyra.adapters.out.persistence.mapper;

import ai.movyra.adapters.out.persistence.entity.AppointmentEntity;
import ai.movyra.domain.model.Appointment;
import ai.movyra.domain.model.valueobject.AppointmentStatus;
import ai.movyra.domain.model.valueobject.MoneyCents;
import ai.movyra.domain.model.valueobject.TenantId;
import ai.movyra.domain.model.valueobject.TimeRange;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;

@ApplicationScoped
public class AppointmentEntityMapper {

    public AppointmentEntity toEntity(Appointment domain) {
        AppointmentEntity entity = new AppointmentEntity();
        entity.setId(domain.getId());
        entity.setTenantId(domain.getTenantId().value());
        entity.setCustomerId(domain.getCustomerId());
        entity.setProfessionalId(domain.getProfessionalId());
        entity.setServiceId(domain.getServiceId());
        entity.setStartAt(domain.getStartAt());
        entity.setEndAt(domain.getEndAt());
        entity.setStatus(domain.getStatus().toDbValue());

        // price can be null in some flows? keep it safe
        entity.setPriceCents(domain.getPrice() != null ? domain.getPrice().value() : null);

        entity.setNotes(domain.getNotes());
        entity.setCreatedByUserId(domain.getCreatedByUserId());

        // preserve domain createdAt (should exist)
        Instant createdAt = domain.getCreatedAt();
        entity.setCreatedAt(createdAt != null ? createdAt : Instant.now());

        return entity;
    }

    public Appointment toDomain(AppointmentEntity entity) {
        TimeRange timeRange = TimeRange.of(entity.getStartAt(), entity.getEndAt());

        Appointment appointment = new Appointment(
                entity.getId(),
                TenantId.of(entity.getTenantId()),
                entity.getCustomerId(),
                entity.getProfessionalId(),
                entity.getServiceId(),
                timeRange,
                AppointmentStatus.fromString(entity.getStatus()),
                MoneyCents.of(entity.getPriceCents() != null ? entity.getPriceCents() : 0)
        );

        appointment.setNotes(entity.getNotes());
        appointment.setCreatedByUserId(entity.getCreatedByUserId());

        // IMPORTANT: do not lose persistence timestamp
        appointment.setCreatedAt(entity.getCreatedAt());

        return appointment;
    }
}
