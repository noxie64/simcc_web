package at.simcc.simcc_backend.repo;

import at.simcc.simcc_backend.entities.TrojanSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 3/27/26
 */
public interface TrojanSessionRepository extends JpaRepository<TrojanSession, Long> {

    Optional<TrojanSession> findTrojanSessionByCcid(String ccid);
}
