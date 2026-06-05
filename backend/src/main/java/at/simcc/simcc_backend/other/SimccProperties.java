package at.simcc.simcc_backend.other;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 6/5/26
 */
@Component
@ConfigurationProperties(prefix = "simcc")
@Data
public class SimccProperties {
    private String dataDir;
}
