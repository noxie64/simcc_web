package at.simcc.simcc_backend.repo;

import at.simcc.simcc_backend.entities.Infected;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/10/26
 */
public interface InfectedRepository extends JpaRepository<Infected, Long> {
    boolean existsInfectedByIid(UUID iid);

    Optional<Infected> findFirstByIid(UUID iid);

    Infected findInfectedByIid(UUID iid);
}