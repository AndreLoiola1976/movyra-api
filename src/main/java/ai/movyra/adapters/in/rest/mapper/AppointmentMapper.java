package ai.movyra.adapters.in.rest.mapper;

import ai.movyra.adapters.in.rest.dto.appointment.AppointmentResponse;
import ai.movyra.adapters.in.rest.dto.appointment.CreateAppointmentRequest;
import ai.movyra.application.port.in.appointment.CreateAppointmentUseCase;
import ai.movyra.domain.model.Appointment;
import ai.movyra.domain.model.valueobject.TenantId;

public class AppointmentMapper {
    
    public static CreateAppointmentUseCase.CreateAppointmentCommand toCommand(
            TenantId tenantId,
            CreateAppointmentRequest request
    ) {
        return new CreateAppointmentUseCase.CreateAppointmentCommand(
            tenantId,
            request.customerId(),
            request.professionalId(),
            request.serviceId(),
            request.startAt(),
            request.endAt(),
            request.priceCents(),
            request.notes()
        );
    }
    
    public static AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
            appointment.getId(),
            appointment.getTenantId().value(),
            appointment.getCustomerId(),
            appointment.getProfessionalId(),
            appointment.getServiceId(),
            appointment.getStartAt(),
            appointment.getEndAt(),
            appointment.getStatus().toDbValue(),
            appointment.getPrice().value(),
            appointment.getNotes(),
            appointment.getCreatedAt()
        );
    }
}
