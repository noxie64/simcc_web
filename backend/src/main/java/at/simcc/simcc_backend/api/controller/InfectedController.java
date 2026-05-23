package at.simcc.simcc_backend.api.controller;

import at.simcc.simcc_backend.api.HTTPError;
import at.simcc.simcc_backend.api.body.CCIDRequest;
import at.simcc.simcc_backend.api.service.InfectedService;
import at.simcc.simcc_backend.models.InfectedIdDto;
import at.simcc.simcc_backend.models.InfectedNoIdDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/10/26
 */
@RestController
@RequestMapping("/infected")
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
}
