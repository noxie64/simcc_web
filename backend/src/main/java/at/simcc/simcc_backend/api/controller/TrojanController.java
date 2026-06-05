package at.simcc.simcc_backend.api.controller;

import at.simcc.simcc_backend.api.body.TrojanCreationRequest;
import at.simcc.simcc_backend.api.service.TrojanService;
import at.simcc.simcc_backend.entities.User;
import at.simcc.simcc_backend.repo.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create")
    public void createTrojan(@Valid @RequestBody TrojanCreationRequest body) {
        User user = userRepo.findUserByUserId(body.userId());

        trojanService.createTrojan(body.name(), body.buildConfig(), user);
    }
}