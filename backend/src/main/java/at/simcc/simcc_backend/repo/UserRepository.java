package at.simcc.simcc_backend.repo;

import at.simcc.simcc_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User getByUserId(Long userId);

    User getByUsername(String username);
}