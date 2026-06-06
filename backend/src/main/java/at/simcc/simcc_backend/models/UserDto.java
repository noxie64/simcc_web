package at.simcc.simcc_backend.models;

import at.simcc.simcc_backend.entities.Role;
import at.simcc.simcc_backend.entities.User;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link User}
 */
@Value
public class UserDto implements Serializable {
    Long userId;
    String username;
    String email;
    LocalDateTime createdAt;
    Set<Role> roles;
}