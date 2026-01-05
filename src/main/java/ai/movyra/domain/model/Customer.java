package ai.movyra.domain.model;

import ai.movyra.domain.model.valueobject.TenantId;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Customer {
    
    private UUID id;
    private TenantId tenantId;
    private String fullName;
    private String phone;
    private String email;
    private Instant createdAt;
    
    public Customer(UUID id, TenantId tenantId, String fullName) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "TenantId cannot be null");
        this.fullName = Objects.requireNonNull(fullName, "Full name cannot be null");
        this.createdAt = Instant.now();
    }
    
    // Getters
    public UUID getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public Instant getCreatedAt() { return createdAt; }
    
    // Setters
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
}
