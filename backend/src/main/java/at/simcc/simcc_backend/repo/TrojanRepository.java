package at.simcc.simcc_backend.repo;

import at.simcc.simcc_backend.entities.Trojan;
import at.simcc.simcc_backend.entities.TrojanBuild;
import at.simcc.simcc_backend.entities.User;
import at.simcc.simcc_backend.models.TrojanDisplayDto;
import at.simcc.simcc_backend.models.TrojanPlainDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 3/27/26
 */
public interface TrojanRepository extends JpaRepository<Trojan, Long> {

    Optional<Trojan> findTrojanByCcid(UUID ccid);

    List<Trojan> findTrojansByCreatedBy(User createdBy);

    @Query("""
SELECT new at.simcc.simcc_backend.models.TrojanDisplayDto(t.ccid, t.name, MAX(b.buildAt), null)
FROM Trojan t LEFT JOIN t.trojanBuilds b
              JOIN t.createdBy u
GROUP BY u, t.ccid, t.name
HAVING u = :user
""")
    List<TrojanDisplayDto> findTrojansForDisplay(User user);
}
