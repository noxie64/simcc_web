package at.simcc.simcc_backend.repo;

import at.simcc.simcc_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 */
public interface UserRepository extends JpaRepository<User, Long> {
}