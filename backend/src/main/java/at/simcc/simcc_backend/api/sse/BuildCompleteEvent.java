package at.simcc.simcc_backend.api.sse;

import java.util.UUID;

public record BuildCompleteEvent(UUID ccid, String message) implements BuildEvent {}
