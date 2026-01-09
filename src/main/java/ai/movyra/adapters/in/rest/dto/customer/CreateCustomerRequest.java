package ai.movyra.adapters.in.rest.dto.customer;

public record CreateCustomerRequest(
    String fullName,
    String phone,
    String email
) {}
