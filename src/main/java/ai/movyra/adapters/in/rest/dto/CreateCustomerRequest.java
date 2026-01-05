package ai.movyra.adapters.in.rest.dto;

public record CreateCustomerRequest(
    String fullName,
    String phone,
    String email
) {}
