package at.simcc.simcc_backend.api.controller;

import at.simcc.simcc_backend.api.HTTPError;
import at.simcc.simcc_backend.api.body.InfectedRegistrationRequest;
import at.simcc.simcc_backend.api.service.InfectedService;
import at.simcc.simcc_backend.models.InfectedDto;
import at.simcc.simcc_backend.models.InfectedIdDto;
import at.simcc.simcc_backend.models.InfectedWithLatestIPDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

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
}
