package ai.movyra.application.port.in;

import ai.movyra.domain.model.Appointment;
import ai.movyra.domain.model.valueobject.TenantId;

import java.time.Instant;
import java.util.List;

/**
 * Use case (inbound port) for listing appointments for a tenant within a time range.
 *
 * Notes for SaaS multi-tenant:
 * - TenantId is mandatory here to prevent accidental cross-tenant access.
 * - Range should be validated at the application layer (not the controller).
 */
public interface FindAppointmentsUseCase {

    /**
     * Returns all appointments for a given tenant within [from, to].
     *
     * @param tenantId Tenant identifier (required)
     * @param from     Range start (inclusive)
     * @param to       Range end (exclusive or inclusive - choose one policy and keep it consistent)
     * @return list of appointments
     */
    List<Appointment> findByTenantAndRange(TenantId tenantId, Instant from, Instant to);
}

