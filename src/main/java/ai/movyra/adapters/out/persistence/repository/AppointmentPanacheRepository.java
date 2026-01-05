package ai.movyra.adapters.out.persistence.repository;

import ai.movyra.adapters.out.persistence.entity.AppointmentEntity;
import ai.movyra.adapters.out.persistence.mapper.AppointmentEntityMapper;
import ai.movyra.application.port.out.AppointmentRepository;
import ai.movyra.domain.model.Appointment;
import ai.movyra.domain.model.valueobject.TenantId;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class AppointmentPanacheRepository
        implements PanacheRepositoryBase<AppointmentEntity, UUID>, AppointmentRepository {

    @Inject
    AppointmentEntityMapper mapper;

    @Override
    public Appointment save(Appointment appointment) {
        AppointmentEntity entity = mapper.toEntity(appointment);

        boolean existsForTenant = find(
                "tenantId = ?1 and id = ?2",
                entity.getTenantId(),
                entity.getId()
        ).firstResultOptional().isPresent();

        AppointmentEntity managed;
        if (existsForTenant) {
            managed = getEntityManager().merge(entity);
        } else {
            persist(entity);
            managed = entity;
        }

        return mapper.toDomain(managed);
    }

    @Override
    public Optional<Appointment> findById(TenantId tenantId, UUID id) {
        return find("tenantId = ?1 and id = ?2", tenantId.value(), id)
                .firstResultOptional()
                .map(mapper::toDomain);
    }

    @Override
    public List<Appointment> findByProfessionalAndTimeRange(TenantId tenantId, UUID professionalId, Instant start, Instant end) {
        return find(
                "tenantId = ?1 and professionalId = ?2 and startAt < ?3 and endAt > ?4",
                tenantId.value(),
                professionalId,
                end,
                start
        ).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Appointment> findByTimeRange(TenantId tenantId, Instant start, Instant end) {
        // agenda overlap
        return find(
                "tenantId = ?1 and startAt < ?2 and endAt > ?3 order by startAt",
                tenantId.value(),
                end,
                start
        ).stream().map(mapper::toDomain).toList();
    }
}
