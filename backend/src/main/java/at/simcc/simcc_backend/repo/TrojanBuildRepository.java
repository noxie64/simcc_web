package at.simcc.simcc_backend.repo;

import at.simcc.simcc_backend.entities.TrojanBuild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 6/7/26
 */
public interface TrojanBuildRepository extends JpaRepository<TrojanBuild, UUID> {

    @Query("""
SELECT b
FROM Trojan t JOIN t.trojanBuilds b
WHERE b.buildAt = (
    SELECT MAX(b1.buildAt)
    FROM TrojanBuild b1
    WHERE b1.trojan.ccid = :ccid
) AND t.ccid = :ccid
""")
    Optional<TrojanBuild> findLatestBuildForTrojan(UUID ccid);
}