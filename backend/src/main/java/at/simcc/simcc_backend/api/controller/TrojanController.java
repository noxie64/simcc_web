package at.simcc.simcc_backend.api.controller;

import at.simcc.simcc_backend.api.body.TrojanCreationRequest;
import at.simcc.simcc_backend.api.service.TrojanService;
import at.simcc.simcc_backend.api.sse.BuildCompleteEvent;
import at.simcc.simcc_backend.api.sse.BuildFailedEvent;
import at.simcc.simcc_backend.api.sse.BuildSSEComponent;
import at.simcc.simcc_backend.entities.Trojan;
import at.simcc.simcc_backend.entities.TrojanBuild;
import at.simcc.simcc_backend.entities.User;
import at.simcc.simcc_backend.entities.trojan_setting.TrojanSettingKey;
import at.simcc.simcc_backend.mapper.TrojanMapper;
import at.simcc.simcc_backend.models.TrojanDisplayDto;
import at.simcc.simcc_backend.other.SimccSettings;
import at.simcc.simcc_backend.repo.TrojanBuildRepository;
import at.simcc.simcc_backend.repo.TrojanRepository;
import at.simcc.simcc_backend.repo.UserRepository;
import at.simcc.simcc_backend.trojan_build.TrojanBuildService;
import io.netty.handler.codec.http.HttpContent;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import tools.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    private final ObjectMapper objMapper;
    private final BuildSSEComponent buildSSEComponent;
    private final TrojanRepository trojanRepo;
    private final TrojanMapper trojanMapper;
    private final TrojanBuildRepository trojanBuildRepo;

    @PostMapping("/create")
    public ResponseEntity<TrojanDisplayDto> createTrojan(@Valid @RequestBody TrojanCreationRequest body, @AuthenticationPrincipal User user) {
        Trojan trojan = trojanService.createTrojan(body.name(), body.buildConfig(), user);

        return ResponseEntity.ok(
                new TrojanDisplayDto(
                        trojan.getCcid(),
                        trojan.getName(),
                        null,
                        false
                )
        );
    }

    @GetMapping("/download/{ccid}")
    public ResponseEntity<byte[]> downloadTrojan(@PathVariable UUID ccid, HttpServletResponse response) {

        Optional<TrojanBuild> trojanBuildOpt = trojanBuildRepo.findLatestBuildForTrojan(ccid);

        if (trojanBuildOpt.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Trojan-build for CCID %s wasn't found!".formatted(ccid)
            );
        }

        TrojanBuild trojanBuild = trojanBuildOpt.get();
        String trojanName = trojanBuild.getTrojan().getName();

        File buildExe = SimccSettings.BUILD_DIR.resolve("%s.exe".formatted(trojanBuild.getBuildId())).toFile();

        if(!buildExe.exists()) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "File for trojan %s doesn't exist on disk!".formatted(ccid)
            );
        }

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        String fileName = "%s-%s"
                .formatted(
                        trojanName.replace(" ", "_"),
                        trojanBuild.getBuildAt().format(DateTimeFormatter.ISO_DATE_TIME)
                );

        try (ZipOutputStream zout = new ZipOutputStream(bout)){
            ZipEntry zipEntry = new ZipEntry("/%s.exe".formatted(fileName));

            zout.putNextEntry(zipEntry);

            Files.copy(buildExe.toPath(), zout);
        } catch (IOException e) {
            log.error(String.valueOf(e));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create zip-file!");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"%s.zip\""
                                .formatted(
                                        fileName
                                )
                )
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bout.toByteArray());

    }

    @GetMapping("/")
    public ResponseEntity<List<TrojanDisplayDto>> listAllTrojans(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                trojanService.loadAllTrojans(user)
        );
    }

    @PostMapping("/build/{ccid}")
    public ResponseEntity<Void> triggerBuild(@PathVariable UUID ccid) {
        try {
            trojanBuildService.buildTrojan(ccid);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/defaults/build")
    public ResponseEntity<Map<TrojanSettingKey, Object>> buildDefaults() {
        return ResponseEntity.ok(
                Arrays.stream(TrojanSettingKey.values())
                        .collect(Collectors.toMap(s -> s, s -> s.defaultValue))
        );
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