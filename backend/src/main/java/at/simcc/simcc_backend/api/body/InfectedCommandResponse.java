package at.simcc.simcc_backend.api.body;

public record InfectedCommandResponse(String stdout, String stderr, Integer statusCode) {
}
