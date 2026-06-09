package at.simcc.simcc_backend.api.controller;

import at.simcc.simcc_backend.api.body.TrojanCreationRequest;
import at.simcc.simcc_backend.api.service.TrojanService;
import at.simcc.simcc_backend.api.sse.BuildCompleteEvent;
import at.simcc.simcc_backend.api.sse.BuildEvent;
import at.simcc.simcc_backend.api.sse.BuildFailedEvent;
import at.simcc.simcc_backend.api.sse.BuildSSEComponent;
import at.simcc.simcc_backend.entities.User;
import at.simcc.simcc_backend.mapper.TrojanMapper;
import at.simcc.simcc_backend.models.TrojanPlainDto;
import at.simcc.simcc_backend.repo.UserRepository;
import at.simcc.simcc_backend.trojan_build.TrojanBuildService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;
import java.util.UUID;

/**
 * Project: simcc_backend
 * Created by: Georg Kollegger
 * Date: 5/25/26
 */
@RestController
@RequestMapping("/trojan")
@RequiredArgsConstructor
@Slf4j
public class TrojanController {
    private final TrojanService trojanService;
    private final UserRepository userRepo;
    private final TrojanBuildService trojanBuildService;
    private final TrojanMapper trojanMapper;
    private final ObjectMapper objMapper;
    private final BuildSSEComponent buildSSEComponent;

    @PostMapping("/create")
    public ResponseEntity<TrojanPlainDto> createTrojan(@Valid @RequestBody TrojanCreationRequest body) {
        User user = userRepo.findUserByUserId(body.userId());

        return ResponseEntity.ok(
            trojanMapper.toDto(trojanService.createTrojan(body.name(), body.buildConfig(), user))
        );
    }

    @PostMapping("/build/{ccid}")
    public ResponseEntity<Void> triggerBuild(@PathVariable UUID ccid) {
        trojanBuildService.buildTrojan(ccid);
        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/sse/build", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamBuildEvents() {
        return buildSSEComponent.getSseSink().asFlux()
                .map(event -> ServerSentEvent.<String>builder()
                        .id(UUID.randomUUID().toString())
                        .event(switch (event) {
                            case BuildFailedEvent _-> "build.failed";
                            case BuildCompleteEvent _ -> "build.completed";
                        })
                        .data(objMapper.writeValueAsString(event))
                        .build()
                );
    }
}