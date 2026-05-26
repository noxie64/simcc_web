package at.simcc.simcc_backend.api.validation;

import at.simcc.simcc_backend.entities.trojan_setting.TrojanSettingKey;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Project: simcc_backend
 * Created by: Georg Kollegger
 * Date: 5/25/26
 */
@Component
@ConfigurationProperties(prefix = "simcc.default")
@Data
public class TrojanBuildConfigDefaults {
    private String host;
    private Long httpCommanderReconnect;
    private Long wsCommanderReconnect;

    @PostConstruct
    private void initEnum() throws IllegalAccessException {
        for (Field f : this.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            String enumName = f.getName()
                    .replaceAll("([a-z])([A-Z])", "$1_$2") // snakecase to camel case
                    .toUpperCase();
            TrojanSettingKey.valueOf(enumName).defaultValue = f.get(this);
        }

        return;
    }
}
