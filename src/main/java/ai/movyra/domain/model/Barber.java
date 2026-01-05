package ai.movyra.domain.model;

import ai.movyra.domain.model.valueobject.TenantId;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Barber {
    
    private UUID id;
    private TenantId tenantId;
    private String displayName;
    private String phone;
    private boolean active;
    private Instant createdAt;
    
    public Barber(UUID id, TenantId tenantId, String displayName) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "TenantId cannot be null");
        this.displayName = Objects.requireNonNull(displayName, "Display name cannot be null");
        this.active = true;
        this.createdAt = Instant.now();
    }
    
    // Getters
    public UUID getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public String getDisplayName() { return displayName; }
    public String getPhone() { return phone; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    
    // Setters
    public void setPhone(String phone) { this.phone = phone; }
    public void setActive(boolean active) { this.active = active; }
}
