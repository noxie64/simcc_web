package at.simcc.simcc_backend.other;

import at.simcc.simcc_backend.entities.trojan_setting.TrojanSettingKey;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.nio.file.Path;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 6/5/26
 */
@Component
@Data
@ConfigurationProperties(prefix = "simcc")
public class SimccConstants {
    public static final Path DATA_DIR = Path.of("/simcc_data");
    public static final Path TROJAN_DIR = DATA_DIR.resolve("simcc_trojan");
    public static final Path BUILD_DIR = DATA_DIR.resolve("build");

    private Docker docker = new Docker();
    private Builder builder = new Builder();

    @Data
    public static class Docker {
        private String host;
        private boolean tls;
    }

    @Data
    public static class Builder {
        private Defaults defaults = new Defaults();

        private String imageTag;

        @Data
        public static class Defaults {
            private String host;
            private Long httpCommanderReconnect;
            private Long wsCommanderReconnect;
        }
    }

    @PostConstruct
    private void initEnum() throws IllegalAccessException {
        for (Field f : Builder.Defaults.class.getDeclaredFields()) {
            f.setAccessible(true);
            String enumName = f.getName()
                    .replaceAll("([a-z])([A-Z])", "$1_$2") // snakecase to camel case
                    .toUpperCase();
            TrojanSettingKey.valueOf(enumName).defaultValue = f.get(builder.defaults);
        }
    }
}
