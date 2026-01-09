package ai.movyra.domain.model;

import ai.movyra.domain.model.valueobject.TenantId;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class PortfolioItem {

    private UUID id;
    private UUID tenantId;

    private String title;
    private String description;
    private String imageUrl;      // OPTIONAL (MVP)
    private Integer sortOrder;

    private boolean active;

    /**
     * Important: must represent persistence truth.
     * Do NOT always set to now() on re-hydration.
     */
    private Instant createdAt;

    // =========================
    // Constructor for NEW items
    // =========================
    public PortfolioItem(UUID tenantId, String title) {
        this.id = UUID.randomUUID();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.title = normalizeTitle(Objects.requireNonNull(title, "title cannot be null"));

        this.description = null;
        this.imageUrl = null;     // optional
        this.sortOrder = 0;

        this.active = true;
        this.createdAt = Instant.now();
    }

    // Optional: allow providing id explicitly for some use cases/tests
    public PortfolioItem(UUID id, UUID tenantId, String title) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.title = normalizeTitle(Objects.requireNonNull(title, "title cannot be null"));

        this.description = null;
        this.imageUrl = null;     // optional
        this.sortOrder = 0;

        this.active = true;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }

    public TenantId getTenantId() {
        return TenantId.of(tenantId);
    }

    // If your mappers prefer raw UUID:
    public UUID getTenantIdValue() { return tenantId; }

    // =========
    // Behavior
    // =========

    public void rename(String newTitle) {
        this.title = normalizeTitle(Objects.requireNonNull(newTitle, "title cannot be null"));
    }

    public void activate() { this.active = true; }

    public void deactivate() { this.active = false; }

    // =========
    // Getters
    // =========

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public Integer getSortOrder() { return sortOrder; }

    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }

    // =========
    // Setters for optional fields
    // =========

    public void setDescription(String description) {
        this.description = blankToNull(description);
    }

    /**
     * MVP: optional field.
     * Accept null or blank; blank becomes null.
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = blankToNull(imageUrl);
    }

    public void setSortOrder(Integer sortOrder) {
        // keep it tolerant for MVP
        this.sortOrder = (sortOrder != null) ? sortOrder : 0;
    }

    // =========
    // Setters used by persistence re-hydration
    // =========

    /**
     * Keep this setter (used by persistence mappers).
     * It is NOT an "optional field"; it's re-hydration.
     */
    public void setActive(boolean active) { this.active = active; }

    /**
     * Critical for persistence re-hydration.
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt cannot be null");
    }

    /**
     * If your persistence layer needs to re-hydrate IDs explicitly.
     * Prefer: only mappers call these (not use cases).
     */
    public void setId(UUID id) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
    }

    // =========
    // Normalization helpers
    // =========

    private static String normalizeTitle(String raw) {
        String s = raw.trim();
        if (s.isBlank()) throw new IllegalArgumentException("title cannot be blank");
        return s;
    }

    private static String blankToNull(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        return s.isBlank() ? null : s;
    }
}
