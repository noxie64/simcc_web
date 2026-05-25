package at.simcc.simcc_backend.api.dal;

import at.simcc.simcc_backend.entities.Infected;
import at.simcc.simcc_backend.mapper.InfectedMapper;
import at.simcc.simcc_backend.models.InfectedIdDto;
import at.simcc.simcc_backend.repo.InfectedRepository;
import at.simcc.simcc_backend.repo.TrojanSessionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/10/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InfectedDal {
    private final TrojanSessionRepository trojanSessionRepository;
    private final InfectedRepository infectedRepository;

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
        InfectedIdDto dto = infectedMapper.toDtoId(saved);
        return dto;
    }
}
