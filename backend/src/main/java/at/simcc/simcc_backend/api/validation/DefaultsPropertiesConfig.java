package at.simcc.simcc_backend.api.validation;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Project: simcc_backend
 * Created by: Georg Kollegger
 * Date: 5/25/26
 */
@Component
@ConfigurationProperties(prefix = "simcc.default")
public class DefaultsPropertiesConfig {
    private String host;
    private Long http_commander_reconnect;
    private Long ws_commander_reconnect;
}
