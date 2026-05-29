package at.simcc.simcc_backend.repo;

import at.simcc.simcc_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getByUsername(String username);

    User getByEmail(String email);

    User getUserByEmail(String email);
}