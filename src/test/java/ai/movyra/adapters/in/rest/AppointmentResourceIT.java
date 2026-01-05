package ai.movyra.adapters.in.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class AppointmentResourceIT {

    private String tenantId;
    private String professionalId;
    private String customerId;
    private String serviceId;

    @BeforeEach
    void setup() {
        // Create tenant (NO X-Tenant required)
        Map<String, Object> tenantRequest = new HashMap<>();
        tenantRequest.put("slug", "demo-" + UUID.randomUUID());
        tenantRequest.put("name", "Demo scheduleShop");

        tenantId = given()
                .contentType(ContentType.JSON)
                .body(tenantRequest)
                .when()
                .post("/api/tenants")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Create professional (X-Tenant required)
        Map<String, Object> professionalRequest = new HashMap<>();
        professionalRequest.put("displayName", "John Professional");
        professionalRequest.put("phone", "555-0100");

        professionalId = given()
                .header("X-Tenant", tenantId)
                .contentType(ContentType.JSON)
                .body(professionalRequest)
                .when()
                .post("/api/professionals")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Create customer (X-Tenant required)
        Map<String, Object> customerRequest = new HashMap<>();
        customerRequest.put("fullName", "Jane Customer");
        customerRequest.put("phone", "555-0200");

        customerId = given()
                .header("X-Tenant", tenantId)
                .contentType(ContentType.JSON)
                .body(customerRequest)
                .when()
                .post("/api/customers")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // A serviceId can be any UUID from catalog. For the test, use a random UUID.
        serviceId = UUID.randomUUID().toString();
    }

    @Test
    void shouldRejectTenantScopedEndpointWithoutHeader() {
        Map<String, Object> professionalRequest = new HashMap<>();
        professionalRequest.put("displayName", "NoTenant Professional");
        professionalRequest.put("phone", "555-9999");

        given()
                .contentType(ContentType.JSON)
                .body(professionalRequest)
                .when()
                .post("/api/professionals")
                .then()
                .statusCode(400)
                .body("error", anyOf(equalTo("Missing X-Tenant header"), containsString("Missing X-Tenant")));
    }

    @Test
    void shouldCreateAppointmentSuccessfully() {
        Instant startAt = Instant.parse("2024-01-15T10:00:00Z");
        Instant endAt = Instant.parse("2024-01-15T11:00:00Z");

        Map<String, Object> request = new HashMap<>();
        request.put("customerId", customerId);
        request.put("professionalId", professionalId);
        request.put("serviceId", serviceId);
        request.put("startAt", startAt.toString());
        request.put("endAt", endAt.toString());
        request.put("priceCents", 5000);

        given()
                .header("X-Tenant", tenantId)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/appointments")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("tenantId", equalTo(tenantId));
    }

    @Test
    void shouldRejectOverlappingAppointmentsForSameProfessional() {
        Instant start1 = Instant.parse("2024-01-15T10:00:00Z");
        Instant end1 = Instant.parse("2024-01-15T11:00:00Z");

        Map<String, Object> req1 = new HashMap<>();
        req1.put("customerId", customerId);
        req1.put("professionalId", professionalId);
        req1.put("serviceId", serviceId);
        req1.put("startAt", start1.toString());
        req1.put("endAt", end1.toString());
        req1.put("priceCents", 5000);

        given()
                .header("X-Tenant", tenantId)
                .contentType(ContentType.JSON)
                .body(req1)
                .when()
                .post("/api/appointments")
                .then()
                .statusCode(201);

        Instant start2 = Instant.parse("2024-01-15T10:30:00Z");
        Instant end2 = Instant.parse("2024-01-15T11:30:00Z");

        Map<String, Object> req2 = new HashMap<>();
        req2.put("customerId", customerId);
        req2.put("professional", professionalId);
        req2.put("serviceId", serviceId);
        req2.put("startAt", start2.toString());
        req2.put("endAt", end2.toString());
        req2.put("priceCents", 5000);

        given()
                .header("X-Tenant", tenantId)
                .contentType(ContentType.JSON)
                .body(req2)
                .when()
                .post("/api/appointments")
                .then()
                .statusCode(anyOf(is(409), is(400))); // depends on how the exception is mapped
    }
}
