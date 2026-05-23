package at.simcc.simcc_backend.api.controller;

import at.simcc.simcc_backend.api.HTTPError;
import at.simcc.simcc_backend.api.body.CCIDRequest;
import at.simcc.simcc_backend.api.body.InfectedOnlineWrapper;
import at.simcc.simcc_backend.api.service.InfectedService;
import at.simcc.simcc_backend.entities.Infected;
import at.simcc.simcc_backend.models.InfectedIdDto;
import at.simcc.simcc_backend.models.InfectedNoIdDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/10/26
 */
@RestController
@RequestMapping("/api/infected")
@RequiredArgsConstructor
public class InfectedController {
    private final InfectedService infectedService;

    /**
     * Route to register a newly infected machine
     * @param ccidReq {@link at.simcc.simcc_backend.api.body.CCIDRequest} containing a {@code ccid} to check
     * @return 200 OK with the newly set {@code iid}
     */
    @PostMapping("/reg")
    public ResponseEntity<?> registerInfected(@RequestBody CCIDRequest ccidReq) {

        try {
            InfectedIdDto infectedId = infectedService.registerInfected(ccidReq.ccid());
            return ResponseEntity.ok(infectedId);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HTTPError(
                    "The ccid was not found."
            ));
        }
    }

    /**
     * Register a Server-Side-Event to get notified when ever the infected goes offline.
     * @param iid {@code iid} of the infected
     * @return event of type {@code is-online} to
     */
    @GetMapping(path = "/register-status/{iid}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<InfectedOnlineWrapper>> streamFlux(@PathVariable UUID iid) {
        Optional<Infected> infected = infectedService.getInfectedComplete(iid);
        InfectedOnlineWrapper infectedOnlineWrapper = new InfectedOnlineWrapper(
                infected.isPresent(),
                infected.isPresent()
                        ? infected.get().isOnline()
                        : null
        );

        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> ServerSentEvent.<InfectedOnlineWrapper> builder()
                    .id(String.valueOf(sequence))
                    .event("is-online")
                    .data(infectedOnlineWrapper)
                    .build()
                );
    }


}
