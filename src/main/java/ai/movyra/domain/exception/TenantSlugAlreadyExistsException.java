package ai.movyra.domain.exception;

public class TenantSlugAlreadyExistsException extends RuntimeException {

    private final String slug;

    public TenantSlugAlreadyExistsException(String slug) {
        super("Tenant slug already exists: " + slug);
        this.slug = slug;
    }

    public String getSlug() {
        return slug;
    }
}
