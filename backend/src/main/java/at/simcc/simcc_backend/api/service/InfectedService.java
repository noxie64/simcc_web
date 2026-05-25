package at.simcc.simcc_backend.api.service;

import at.simcc.simcc_backend.api.dal.InfectedDal;
import at.simcc.simcc_backend.models.InfectedIdDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/10/26
 */
@Service
@RequiredArgsConstructor
public class InfectedService {
    private final InfectedDal infectedDal;


    public InfectedIdDto registerInfected(String ccid) {
        return infectedDal.registerInfected(ccid);
    }

}
