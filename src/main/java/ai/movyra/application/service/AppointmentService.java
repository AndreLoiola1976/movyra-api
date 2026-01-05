package ai.movyra.application.service;

import ai.movyra.application.port.in.CreateAppointmentUseCase;
import ai.movyra.application.port.in.FindAppointmentsUseCase;
import ai.movyra.application.port.out.AppointmentRepository;
import ai.movyra.domain.model.Appointment;
import ai.movyra.domain.model.valueobject.AppointmentStatus;
import ai.movyra.domain.model.valueobject.MoneyCents;
import ai.movyra.domain.model.valueobject.TenantId;
import ai.movyra.domain.model.valueobject.TimeRange;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class AppointmentService implements CreateAppointmentUseCase, FindAppointmentsUseCase {

    private final AppointmentRepository appointmentRepository;

    @Inject
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = Objects.requireNonNull(appointmentRepository, "appointmentRepository must not be null");
    }

    // CDI proxy constructor
    protected AppointmentService() {
        this.appointmentRepository = null;
    }

    @Override
    public Appointment create(CreateAppointmentCommand command) {
        Objects.requireNonNull(command, "command must not be null");
        Objects.requireNonNull(command.tenantId(), "tenantId must not be null");
        Objects.requireNonNull(command.startAt(), "startAt must not be null");
        Objects.requireNonNull(command.endAt(), "endAt must not be null");

        if (!command.endAt().isAfter(command.startAt())) {
            throw new IllegalArgumentException("Invalid range: endAt must be after startAt");
        }

        TimeRange range = TimeRange.of(command.startAt(), command.endAt());

        Appointment appointment = new Appointment(
                UUID.randomUUID(),
                command.tenantId(),
                command.customerId(),
                command.barberId(),
                command.serviceId(),
                range,
                AppointmentStatus.REQUESTED,
                MoneyCents.of(command.priceCents())
        );
        appointment.setNotes(command.notes());

        // Conflict check only makes sense if barberId exists
        if (command.barberId() != null) {
            List<Appointment> existing = appointmentRepository.findByBarberAndTimeRange(
                    command.tenantId(),
                    command.barberId(),
                    command.startAt(),
                    command.endAt()
            );
            appointment.checkConflict(existing);
        }

        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> findByTenantAndRange(TenantId tenantId, Instant from, Instant to) {
        Objects.requireNonNull(tenantId, "tenantId must not be null");
        Objects.requireNonNull(from, "from must not be null");
        Objects.requireNonNull(to, "to must not be null");

        if (!to.isAfter(from)) {
            throw new IllegalArgumentException("Invalid range: to must be after from");
        }

        // Agenda: overlap, não “contained within”.
        return appointmentRepository.findByTimeRange(tenantId, from, to);
    }
}
