package at.simcc.simcc_backend.api.sse;

import lombok.Builder;

import java.util.UUID;

public record BuildFailedEvent(UUID ccid, String message) implements BuildEvent {}
