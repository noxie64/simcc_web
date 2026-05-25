package at.simcc.simcc_backend.entities.trojan_setting;

import lombok.AllArgsConstructor;

import java.util.function.Function;

/**
 * Project: simcc_backend
 * Created by: Georg Kollegger
 * Date: 5/25/26
 */
@AllArgsConstructor
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

}
