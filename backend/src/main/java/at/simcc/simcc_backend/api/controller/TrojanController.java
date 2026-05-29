package at.simcc.simcc_backend.api.controller;

import at.simcc.simcc_backend.api.body.TrojanCreationRequest;
import at.simcc.simcc_backend.api.service.TrojanService;
import at.simcc.simcc_backend.entities.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/create")
    public void createTrojan(User user, @Valid @RequestBody TrojanCreationRequest body) {
        trojanService.createTrojan(body.name(), body.buildConfig(), user);
    }

}
