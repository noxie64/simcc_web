package at.simcc.simcc_backend.entities.trojan_setting;

import at.simcc.simcc_backend.api.validation.TrojanBuildConfigDefaults;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * Project: simcc_backend
 * Created by: Georg Kollegger
 * Date: 5/25/26
 */
public enum TrojanSettingKey {

    HOST {
        @Override
        public Class<?> expectedType() {
            return String.class;
        }
    },
    HTTP_COMMANDER_RECONNECT {
        @Override
        public Class<?> expectedType() {
            return Long.class;
        }
    },
    WS_COMMANDER_RECONNECT {
        @Override
        public Class<?> expectedType() {
            return Long.class;
        }
    };

    public abstract Class<?> expectedType();
    public Object defaultValue;
}
