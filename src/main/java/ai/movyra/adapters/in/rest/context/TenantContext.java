package ai.movyra.adapters.in.rest.context;

import ai.movyra.domain.model.valueobject.TenantId;
import jakarta.enterprise.context.RequestScoped;

import java.util.UUID;

@RequestScoped
public class TenantContext {
    
    private TenantId tenantId;
    
    public TenantId getTenantId() {
        if (tenantId == null) {
            throw new IllegalStateException("TenantId not set in context");
        }
        return tenantId;
    }
    
    public void setTenantId(UUID tenantId) {
        this.tenantId = TenantId.of(tenantId);
    }
    
    public void setTenantId(TenantId tenantId) {
        this.tenantId = tenantId;
    }
    
    public boolean isSet() {
        return tenantId != null;
    }
}
