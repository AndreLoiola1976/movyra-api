package ai.movyra.application.service.tenant;

import ai.movyra.application.port.in.tenant.*;
import ai.movyra.application.port.out.TenantRepository;
import ai.movyra.domain.exception.TenantNotFoundException;
import ai.movyra.domain.model.Tenant;
import ai.movyra.domain.exception.TenantSlugAlreadyExistsException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import ai.movyra.application.port.in.tenant.UpdateTenantUseCase;
import ai.movyra.application.port.in.tenant.UpdateTenantCommand;


import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TenantService implements CreateTenantUseCase, GetTenantUseCase, ListTenantUseCase, DeactivateTenantUseCase, UpdateTenantUseCase {

    private final TenantRepository tenantRepository;

    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = Objects.requireNonNull(tenantRepository, "tenantRepository must not be null");
    }

    @Override
    @Transactional
    public Tenant getById(UUID tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new TenantNotFoundException(tenantId));

        if (!tenant.isActive()) {
            throw new TenantNotFoundException(tenantId);
        }

        return tenant;
    }

    @Override
    @Transactional
    public List<Tenant> listAllTenants() {
        return tenantRepository.findAllActive();
    }

    @Override
    public void deactivate(UUID id) {
        // idempotent: if not found, throw 404 (we’ll map it), if already inactive, do nothing
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new TenantNotFoundException(id));

        if (!tenant.isActive()) {
            return;
        }

        tenantRepository.deactivate(id);
    }

    @Override
    @Transactional
    public List<Tenant> listActivePage(int page, int size, String sort, boolean desc) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100); // limite 100
        int offset = safePage * safeSize;

        return tenantRepository.findActivePage(offset, safeSize, sort, desc);
    }

    @Override
    @Transactional
    public long countActiveTenants() {
        return tenantRepository.countActive();
    }

    @Override
    @Transactional
    public Tenant update(UpdateTenantCommand command) {
        Objects.requireNonNull(command, "command must not be null");
        Objects.requireNonNull(command.tenantId(), "tenantId must not be null");

        Tenant tenant = tenantRepository.findById(command.tenantId())
                .orElseThrow(() -> new TenantNotFoundException(command.tenantId()));

        if (!tenant.isActive()) {
            throw new TenantNotFoundException(command.tenantId());
        }

        // name (if present must be non-blank)
        if (command.name() != null) {
            String name = command.name().trim();
            if (name.isBlank()) {
                throw new IllegalArgumentException("name must not be blank");
            }
            // assuming Tenant has setName (if not, compile will tell us and we adjust in 1 step)
            tenant.rename(name);
        }

        // phone (null = keep, blank = clear)
        if (command.phone() != null) {
            String phone = command.phone().trim();
            tenant.setPhone(phone.isBlank() ? null : phone);
        }

        // timezone (if present validate)
        if (command.timezone() != null) {
            String tz = normalizeTimezone(command.timezone());
            tenant.changeTimezone(tz);
        }

        return tenantRepository.save(tenant);
    }

    @Override
    @Transactional
    public Tenant create(CreateTenantCommand command) {
        Objects.requireNonNull(command, "command must not be null");
        requireNonBlank(command.slug(), "slug must not be blank");
        requireNonBlank(command.name(), "name must not be blank");

        String slug = normalizeSlug(command.slug());
        String name = command.name().trim();

        String country = normalizeCountry(command.country());
        String timezone = normalizeTimezone(command.timezone());

        // SaaS: slug é identidade pública (URL/tenant routing). Duplicidade é conflito.
        Optional<Tenant> existing = tenantRepository.findBySlug(slug);
        if (existing.isPresent()) {
            throw new TenantSlugAlreadyExistsException(slug);
        }

        Tenant tenant = new Tenant(
                UUID.randomUUID(),
                slug,
                name,
                country,
                timezone
        );

        if (command.phone() != null && !command.phone().isBlank()) {
            tenant.setPhone(command.phone().trim());
        }

        return tenantRepository.save(tenant);
    }

    private static void requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Slug stable, url-safe, lowercase.
     * Rules:
     * - trim
     * - lower
     * - spaces -> hyphen
     * - keep [a-z0-9-]
     */
    private static String normalizeSlug(String raw) {
        String s = raw.trim().toLowerCase(Locale.ROOT);
        s = s.replaceAll("\\s+", "-");
        s = s.replaceAll("[^a-z0-9\\-]", "");
        if (s.isBlank()) {
            throw new IllegalArgumentException("slug must contain letters/numbers");
        }
        return s;
    }

    private static String normalizeCountry(String raw) {
        if (raw == null || raw.isBlank()) return "US";
        String s = raw.trim().toUpperCase(Locale.ROOT);
        if (s.length() != 2) {
            throw new IllegalArgumentException("country must be a 2-letter ISO code (e.g., US, BR)");
        }
        return s;
    }

    private static String normalizeTimezone(String raw) {
        String tz = (raw == null || raw.isBlank()) ? "America/New_York" : raw.trim();
        // Guardrail leve: valida se é IANA zone id. Isso evita lixo no banco.
        try {
            ZoneId.of(tz);
            return tz;
        } catch (Exception e) {
            throw new IllegalArgumentException("timezone must be a valid IANA zone id (e.g., America/New_York)");
        }
    }
}
