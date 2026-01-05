# Movyra API

Multi-tenant SaaS scheduling API for barbershops, salons and other professionals.

## Architecture

This project follows **Clean Architecture / Hexagonal Architecture** with **Domain-Driven Design (DDD)** principles.

### Layers

- **Domain** (`ai.movyra.domain`): Pure business logic, no framework dependencies
- **Application** (`ai.movyra.application`): Use cases and port interfaces
- **Adapters** (`ai.movyra.adapters`): Framework integrations (REST, Persistence)

### Technology Stack

- Java 21
- Quarkus 3.17
- PostgreSQL 16
- Flyway (migrations)
- Hibernate ORM with Panache (Repository Pattern)
- JUnit 5 + Mockito + AssertJ + RestAssured

## Running Locally

### Prerequisites

- Java 21
- Maven 3.9+
- Docker & Docker Compose

### Development Mode

```bash
./mvnw quarkus:dev
```

### With Docker Compose

```bash
docker-compose up --build
```

The API will be available at `http://localhost:8080`

## Testing

```bash
./mvnw test
```

## Multi-Tenancy

All API requests (except `/api/tenants`) must include the `X-Tenant` header with a valid tenant UUID.

Example:
```bash
curl -H "X-Tenant: 123e4567-e89b-12d3-a456-426614174000" \
     http://localhost:8080/api/appointments
```

## API Endpoints

- `POST /api/tenants` - Create tenant
- `POST /api/barbers` - Create barber (requires X-Tenant)
- `POST /api/customers` - Create customer (requires X-Tenant)
- `POST /api/appointments` - Create appointment (requires X-Tenant)
- `GET /api/appointments?from=&to=` - List appointments (requires X-Tenant)
- `GET /q/health` - Health check

## License

Proprietary
