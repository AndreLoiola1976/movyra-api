package ai.movyra.application.port.in.tenant;

import ai.movyra.domain.model.Tenant;

import java.util.List;

public interface ListTenantUseCase {

    List<Tenant> listAllTenants();

    List<Tenant> listActivePage(int page, int size, String sort, boolean desc);

    long countActiveTenants();

}
