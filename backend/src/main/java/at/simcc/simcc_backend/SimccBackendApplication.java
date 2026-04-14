package at.simcc.simcc_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.SecureRandom;

@SpringBootApplication
public class SimccBackendApplication {

    public static final SecureRandom RANDOM = new SecureRandom();

    public static void main(String[] args) {
        SpringApplication.run(SimccBackendApplication.class, args);
    }

}
