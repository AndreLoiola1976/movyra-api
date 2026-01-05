package ai.movyra.adapters.in.rest.filter;

import ai.movyra.adapters.in.rest.context.TenantContext;
import ai.movyra.adapters.in.rest.tenant.TenantScoped;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.lang.reflect.Method;
import java.util.UUID;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class TenantFilter implements ContainerRequestFilter {

    private static final String TENANT_HEADER = "X-Tenant";

    @Inject
    TenantContext tenantContext;

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (!isTenantRequired()) {
            return;
        }

        String tenantHeader = requestContext.getHeaderString(TENANT_HEADER);
        if (tenantHeader == null || tenantHeader.isBlank()) {
            requestContext.abortWith(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\": \"Missing X-Tenant header\"}")
                            .build()
            );
            return;
        }

        try {
            UUID tenantId = UUID.fromString(tenantHeader.trim());
            tenantContext.setTenantId(tenantId);
        } catch (IllegalArgumentException e) {
            requestContext.abortWith(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\": \"Invalid X-Tenant header format\"}")
                            .build()
            );
        }
    }

    private boolean isTenantRequired() {
        if (resourceInfo == null) {
            return false;
        }
        Method method = resourceInfo.getResourceMethod();
        Class<?> clazz = resourceInfo.getResourceClass();

        boolean methodScoped = method != null && method.isAnnotationPresent(TenantScoped.class);
        boolean classScoped = clazz != null && clazz.isAnnotationPresent(TenantScoped.class);

        return methodScoped || classScoped;
    }
}
