package at.simcc.simcc_backend.db;

import at.simcc.simcc_backend.entities.TrojanSession;
import at.simcc.simcc_backend.entities.User;
import at.simcc.simcc_backend.repo.TrojanSessionRepository;
import at.simcc.simcc_backend.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import static at.simcc.simcc_backend.SimccBackendApplication.RANDOM;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 3/27/26
 */
@Component
@RequiredArgsConstructor
public class DBManager implements ApplicationRunner {
    private final TrojanSessionRepository trojanSessionRepo;
    private final UserRepository userRepo;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String value;

    private void initDb() {
        User user = User.builder()
                .username("admin")
                .email("admin@admin.com")
                .password("should_be_a_hash")
                .build();


        byte[] ccidBytes = new byte[32];
        RANDOM.nextBytes(ccidBytes);

        TrojanSession trojanSession = TrojanSession.builder()
                .ccid(Base64.getUrlEncoder().withoutPadding().encodeToString(ccidBytes))
                .createdBy(user)
                .build();

        trojanSessionRepo.save(trojanSession);

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (value.equals("create")) initDb();
    }
}
