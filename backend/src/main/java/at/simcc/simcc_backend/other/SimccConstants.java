package at.simcc.simcc_backend.other;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 6/5/26
 */
@Component
@Data
public class SimccConstants {
    public static final Path DATA_DIR = Path.of("/simcc_data");
    public static final Path TROJAN_DIR = DATA_DIR.resolve("simcc_trojan");
    public static final Path BUILD_DIR = DATA_DIR.resolve("build");
}
