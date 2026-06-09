package at.simcc.simcc_backend.api.sse;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

public sealed interface BuildEvent permits BuildCompleteEvent, BuildFailedEvent {}

