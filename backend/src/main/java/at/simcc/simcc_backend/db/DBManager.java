package at.simcc.simcc_backend.db;

import at.simcc.simcc_backend.entities.Trojan;
import at.simcc.simcc_backend.entities.User;
import at.simcc.simcc_backend.repo.TrojanRepository;
import at.simcc.simcc_backend.entities.*;
import at.simcc.simcc_backend.repo.InfectedRepository;
import at.simcc.simcc_backend.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
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
    private final TrojanRepository trojanRepository;
    private final InfectedRepository infectedRepo;
    private final UserRepository userRepo;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String value;

    private void initDb() throws UnknownHostException {
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

        if (userRepo.getByUsername(user.getUsername()).isEmpty()) {
            trojanRepository.save(trojan);
        }
        Inet4Address ipAddressOne = (Inet4Address) Inet4Address.getByName("192.168.1.1");

        List<InfectedIP> infectedIPSOne = new ArrayList<>();
        infectedIPSOne.add(InfectedIP.builder()
                .ip(ipAddressOne)
                .since(LocalDate.now())
                .infected(null)
                .build());

        Infected infectedOne = Infected.builder()
                .osType(OSType.Windows)
                .osSubType("11")
                .osInfo("Up to dated")
                .infectedIPS(infectedIPSOne)
                .build();

        infectedIPSOne.getFirst().setInfected(infectedOne);

        Inet4Address ipAddressTwo = (Inet4Address) Inet4Address.getByName("192.168.1.2");

        List<InfectedIP> infectedIPSTwo = new ArrayList<>();
        infectedIPSTwo.add(InfectedIP.builder()
                .ip(ipAddressTwo)
                .since(LocalDate.now())
                .infected(null)
                .build());

        Infected infectedTwo = Infected.builder()
                .osType(OSType.Windows)
                .osSubType("7")
                .osInfo("Forever young")
                .infectedIPS(infectedIPSTwo)
                .build();

        infectedIPSTwo.getFirst().setInfected(infectedTwo);

        List<Infected> infects = new ArrayList<>();
        infects.add(infectedOne);
        infects.add(infectedTwo);

        infectedRepo.saveAll(infects);

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (value.equals("create")) initDb();
    }
}
