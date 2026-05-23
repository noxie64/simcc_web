package at.simcc.simcc_backend.api.service;

import at.simcc.simcc_backend.entities.Infected;
import at.simcc.simcc_backend.mapper.InfectedMapper;
import at.simcc.simcc_backend.models.InfectedIdDto;
import at.simcc.simcc_backend.repo.InfectedRepository;
import at.simcc.simcc_backend.repo.TrojanSessionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/10/26
 */
@Service
@RequiredArgsConstructor
public class InfectedService {
    private final InfectedRepository infectedRepository;
    private final TrojanSessionRepository trojanSessionRepository;

    private final InfectedMapper infectedMapper;

    /**
     * Register a new infected machine using a {@code ccid}
     * @param ccid a {@code ccid} used to check wether a valid trojan session is used
     * @throws EntityNotFoundException when the {@code ccid} was invalid
     */
    public InfectedIdDto registerInfected(String ccid) {
        trojanSessionRepository.findTrojanSessionByCcid(ccid)
                .orElseThrow(EntityNotFoundException::new);
        Infected saved = infectedRepository.save(new Infected());
        return infectedMapper.toDtoId(saved);
    }

    public Optional<Infected> getInfectedComplete(UUID iid) {
        List<Infected> infectedList =  infectedRepository.findFirstByIid(iid);
        if (infectedList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(infectedList.getFirst());
    }
}
