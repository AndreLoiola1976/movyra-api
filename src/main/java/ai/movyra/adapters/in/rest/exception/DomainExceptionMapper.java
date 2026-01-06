package ai.movyra.adapters.in.rest.exception;

import ai.movyra.domain.exception.AppointmentConflictException;
import ai.movyra.domain.exception.DomainException;
import ai.movyra.domain.exception.TenantNotFoundException;
import ai.movyra.domain.exception.TenantSlugAlreadyExistsException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class DomainExceptionMapper implements ExceptionMapper<DomainException> {

    @Override
    public Response toResponse(DomainException ex) {

        if (ex instanceof TenantNotFoundException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", ex.getMessage()))
                    .build();
        }

        if (ex instanceof TenantSlugAlreadyExistsException || ex instanceof AppointmentConflictException) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", ex.getMessage()))
                    .build();
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }
}
