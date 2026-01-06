package ai.movyra.domain.model;

import ai.movyra.domain.model.valueobject.TenantId;

import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class Tenant {

    private UUID id;
    private String slug;
    private String name;

    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zip;

    private String country;
    private String timezone;

    private String billingEmail;
    private String billingName;

    /**
     * Keep as String for now (to avoid rippling changes).
     * Later: promote to a value object / enum (Trial/Active/PastDue/Cancelled).
     */
    private String billingStatus;

    private boolean active;

    /**
     * Important: must represent persistence truth.
     * Do NOT always set to now() on re-hydration.
     */
    private Instant createdAt;

    public Tenant(UUID id, String slug, String name, String country, String timezone) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.slug = normalizeSlug(Objects.requireNonNull(slug, "slug cannot be null"));
        this.name = normalizeName(Objects.requireNonNull(name, "name cannot be null"));

        this.country = normalizeCountry(Objects.requireNonNull(country, "country cannot be null"));
        this.timezone = normalizeTimezone(Objects.requireNonNull(timezone, "timezone cannot be null"));

        this.billingStatus = "trial";
        this.active = true;
        this.createdAt = Instant.now();
    }

    public TenantId getTenantId() {
        return TenantId.of(id);
    }

    // =========
    // Behavior
    // =========

    public void rename(String newName) {
        this.name = normalizeName(Objects.requireNonNull(newName, "name cannot be null"));
    }

    /**
     * In SaaS, changing slug is a big deal (public URL, integrations).
     * Keep it explicit.
     */
    public void changeSlug(String newSlug) {
        this.slug = normalizeSlug(Objects.requireNonNull(newSlug, "slug cannot be null"));
    }

    public void changeTimezone(String newTimezone) {
        this.timezone = normalizeTimezone(Objects.requireNonNull(newTimezone, "timezone cannot be null"));
    }


    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void setBillingStatus(String billingStatus) {
        if (billingStatus == null || billingStatus.isBlank()) {
            throw new IllegalArgumentException("billingStatus cannot be blank");
        }
        // minimal guardrail aligned with your DB check constraint
        String s = billingStatus.trim().toLowerCase(Locale.ROOT);
        if (!s.equals("trial") && !s.equals("active") && !s.equals("past_due") && !s.equals("cancelled")) {
            throw new IllegalArgumentException("Invalid billingStatus: " + billingStatus);
        }
        this.billingStatus = s;
    }

    // =========
    // Getters
    // =========

    public UUID getId() { return id; }
    public String getSlug() { return slug; }
    public String getName() { return name; }

    public String getPhone() { return phone; }
    public String getAddressLine1() { return addressLine1; }
    public String getAddressLine2() { return addressLine2; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZip() { return zip; }

    public String getCountry() { return country; }
    public String getTimezone() { return timezone; }

    public String getBillingEmail() { return billingEmail; }
    public String getBillingName() { return billingName; }
    public String getBillingStatus() { return billingStatus; }

    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }

    // =========
    // Setters for optional fields
    // =========

    public void setPhone(String phone) { this.phone = blankToNull(phone); }

    public void setAddressLine1(String addressLine1) { this.addressLine1 = blankToNull(addressLine1); }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = blankToNull(addressLine2); }
    public void setCity(String city) { this.city = blankToNull(city); }
    public void setState(String state) { this.state = blankToNull(state); }
    public void setZip(String zip) { this.zip = blankToNull(zip); }

    public void setBillingEmail(String billingEmail) { this.billingEmail = blankToNull(billingEmail); }
    public void setBillingName(String billingName) { this.billingName = blankToNull(billingName); }

    /**
     * Keep this setter (used by persistence mappers).
     * It is NOT an "optional field"; it's re-hydration.
     */
    public void setActive(boolean active) { this.active = active; }

    /**
     * Critical for persistence re-hydration.
     * Your previous model had no way to restore createdAt from DB.
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt cannot be null");
    }

    // =========
    // Normalization helpers
    // =========

    private static String normalizeName(String raw) {
        String s = raw.trim();
        if (s.isBlank()) throw new IllegalArgumentException("name cannot be blank");
        return s;
    }

    private static String normalizeSlug(String raw) {
        String s = raw.trim().toLowerCase(Locale.ROOT);
        s = s.replaceAll("\\s+", "-");
        s = s.replaceAll("[^a-z0-9\\-]", "");
        if (s.isBlank()) throw new IllegalArgumentException("slug must contain letters/numbers");
        return s;
    }

    private static String normalizeCountry(String raw) {
        String s = raw.trim().toUpperCase(Locale.ROOT);
        if (s.length() != 2) throw new IllegalArgumentException("country must be ISO-3166 alpha-2 (2 chars)");
        return s;
    }

    private static String normalizeTimezone(String raw) {
        String s = raw.trim();
        if (s.isBlank()) throw new IllegalArgumentException("timezone cannot be blank");
        return s;
    }

    private static String blankToNull(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        return s.isBlank() ? null : s;
    }
}
