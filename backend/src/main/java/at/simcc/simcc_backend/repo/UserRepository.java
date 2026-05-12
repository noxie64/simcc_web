package at.simcc.simcc_backend.repo;

import at.simcc.simcc_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User getByUsername(String username);

    User getByEmail(String email);

    User getUserByEmail(String email);
}