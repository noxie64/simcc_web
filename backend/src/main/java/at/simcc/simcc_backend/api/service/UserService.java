package at.simcc.simcc_backend.api.service;

import at.simcc.simcc_backend.entities.User;
import at.simcc.simcc_backend.mapper.UserLoginDtoMapper;
import at.simcc.simcc_backend.models.UserLoginDto;
import at.simcc.simcc_backend.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Project: simcc_web
 * Created by: Marko Kushlyk
 * Date: 27.03.2026
 * Time: 10:17
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final UserLoginDtoMapper userLoginAdminMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserLoginDto registerUser(UserLoginDto loginUser) {
        String password = loginUser.getPassword();
        String hashed = passwordEncoder.encode(password);
        loginUser.setPassword(hashed);
        loginUser.setCreatedAt(Instant.now());
        loginUser.setTotpSecret("");
        User user = userLoginAdminMapper.toEntity(loginUser);
        user.setIsAdmin(true);
        userRepo.save(user);
        return userLoginAdminMapper.toDto(user);
    }
}
