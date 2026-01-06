package ai.movyra.domain.exception;

import java.util.UUID;

public class TenantNotFoundException extends DomainException {

    public TenantNotFoundException(UUID tenantId) {
        super("Tenant not found: " + tenantId);
    }
}
