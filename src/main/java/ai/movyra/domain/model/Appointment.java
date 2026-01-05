package ai.movyra.domain.model;

import ai.movyra.domain.exception.AppointmentConflictException;
import ai.movyra.domain.model.valueobject.AppointmentStatus;
import ai.movyra.domain.model.valueobject.MoneyCents;
import ai.movyra.domain.model.valueobject.TenantId;
import ai.movyra.domain.model.valueobject.TimeRange;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Appointment {

    private UUID id;
    private TenantId tenantId;
    private UUID customerId;
    private UUID barberId;
    private UUID serviceId;
    private TimeRange timeRange;
    private AppointmentStatus status;
    private MoneyCents price;
    private String notes;
    private UUID createdByUserId;
    private Instant createdAt;

    public Appointment(
            UUID id,
            TenantId tenantId,
            UUID customerId,
            UUID barberId,
            UUID serviceId,
            TimeRange timeRange,
            AppointmentStatus status,
            MoneyCents price
    ) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "TenantId cannot be null");
        this.timeRange = Objects.requireNonNull(timeRange, "TimeRange cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.price = Objects.requireNonNull(price, "Price cannot be null");

        this.customerId = customerId;
        this.barberId = barberId;
        this.serviceId = serviceId;

        // default, can be overridden when hydrating from persistence
        this.createdAt = Instant.now();
    }

    public void checkConflict(List<Appointment> existingAppointments) {
        if (barberId == null) {
            return;
        }

        for (Appointment existing : existingAppointments) {
            if (existing.barberId != null &&
                    existing.barberId.equals(this.barberId) &&
                    !existing.id.equals(this.id) &&
                    existing.isActiveStatus() &&
                    existing.timeRange.overlaps(this.timeRange)) {

                throw new AppointmentConflictException(barberId, timeRange.toString());
            }
        }
    }

    public boolean isActiveStatus() {
        return status == AppointmentStatus.REQUESTED ||
                status == AppointmentStatus.CONFIRMED;
    }

    // Getters
    public UUID getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public UUID getCustomerId() { return customerId; }
    public UUID getBarberId() { return barberId; }
    public UUID getServiceId() { return serviceId; }
    public TimeRange getTimeRange() { return timeRange; }
    public Instant getStartAt() { return timeRange.startAt(); }
    public Instant getEndAt() { return timeRange.endAt(); }
    public AppointmentStatus getStatus() { return status; }
    public MoneyCents getPrice() { return price; }
    public String getNotes() { return notes; }
    public UUID getCreatedByUserId() { return createdByUserId; }
    public Instant getCreatedAt() { return createdAt; }

    // Setters
    public void setNotes(String notes) { this.notes = notes; }
    public void setCreatedByUserId(UUID createdByUserId) { this.createdByUserId = createdByUserId; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

    public void setCreatedAt(Instant createdAt) {
        // SaaS: timestamps are audit data; never accept null silently.
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt cannot be null");
    }
}
