package ai.movyra.adapters.in.rest.resource;

import ai.movyra.adapters.in.rest.context.TenantContext;
import ai.movyra.adapters.in.rest.dto.appointment.CreateAppointmentRequest;
import ai.movyra.adapters.in.rest.dto.appointment.AppointmentResponse;
import ai.movyra.adapters.in.rest.mapper.AppointmentMapper;
import ai.movyra.adapters.in.rest.tenant.TenantScoped;
import ai.movyra.application.port.in.appointment.CreateAppointmentUseCase;
import ai.movyra.application.port.in.appointment.FindAppointmentsUseCase;
import ai.movyra.domain.model.Appointment;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.List;

@Path("/api/appointments")
@Produces(MediaType.APPLICATION_JSON)
@TenantScoped
public class AppointmentResource {

    @Inject
    TenantContext tenantContext;

    @Inject
    CreateAppointmentUseCase createAppointmentUseCase;

    @Inject
    FindAppointmentsUseCase findAppointmentsUseCase;

    @POST
    public Response create(CreateAppointmentRequest request) {
        Appointment appointment = createAppointmentUseCase.create(
                AppointmentMapper.toCommand(tenantContext.getTenantId(), request)
        );
        AppointmentResponse response = AppointmentMapper.toResponse(appointment);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    public Response find(@QueryParam("from") String from, @QueryParam("to") String to) {
        Instant fromInstant = Instant.parse(from);
        Instant toInstant = Instant.parse(to);

        List<Appointment> appointments = findAppointmentsUseCase.findByTenantAndRange(
                tenantContext.getTenantId(),
                fromInstant,
                toInstant
        );

        List<AppointmentResponse> response = appointments.stream()
                .map(AppointmentMapper::toResponse)
                .toList();

        return Response.ok(response).build();
    }
}
