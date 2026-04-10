package at.simcc.simcc_backend.repo;

import at.simcc.simcc_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Project: simcc_web
 * Created by: Marko Kushlyk
 * Date: 27.03.2026
 * Time: 10:59
 */
public interface UserRepository extends JpaRepository<User, Long> {

}
