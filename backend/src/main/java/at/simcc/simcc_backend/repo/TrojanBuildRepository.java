package at.simcc.simcc_backend.repo;

import at.simcc.simcc_backend.entities.TrojanBuild;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 6/7/26
 */
public interface TrojanBuildRepository extends JpaRepository<TrojanBuild, UUID> {
}