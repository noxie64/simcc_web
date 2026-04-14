package at.simcc.simcc_backend.repo;

import at.simcc.simcc_backend.entities.Infected;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/10/26
 */
public interface InfectedRepository extends JpaRepository<Infected, Long> {
    boolean existsInfectedByIid(Long iid);
}