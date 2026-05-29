package at.simcc.simcc_backend.db;

import at.simcc.simcc_backend.entities.Trojan;
import at.simcc.simcc_backend.entities.User;
import at.simcc.simcc_backend.repo.TrojanRepository;
import at.simcc.simcc_backend.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Base64;

import static at.simcc.simcc_backend.SimccBackendApplication.RANDOM;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 3/27/26
 */
@Component
@RequiredArgsConstructor
public class DBManager implements ApplicationRunner {
    private final TrojanRepository trojanRepository;
    private final UserRepository userRepo;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String value;

    private void initDb() {
        User user = User.builder()
                .username("admin")
                .email("admin@admin.com")
                .password("should_be_a_hash")
                .isAdmin(false)
                .build();

        Trojan trojan = Trojan.builder()
                .name("Test")
                .createdBy(user)
                .build();

        trojanRepository.save(trojan);

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (value.equals("create")) initDb();
    }
}
