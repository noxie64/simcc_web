package at.simcc.simcc_backend.entities.trojan_setting;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Project: simcc_backend
 * Created by: Georg Kollegger
 * Date: 5/25/26
 */
public enum TrojanSettingKey {

    HOST {
        private static final Pattern HOST_PATTERN = java.util.regex.Pattern.compile("^\\w*(:\\d{1,5})?$");
        @Override
        public boolean validate(Object value) {
            if (value.getClass() != String.class) return false;
            String concreteValue = (String) value;

            return HOST_PATTERN.matcher(concreteValue).find();
        }
    },
    HTTP_COMMANDER_RECONNECT {
        @Override
        public boolean validate(Object value) {
            return validateNumber(value, 1, Long.MAX_VALUE);
        }
    },
    WS_COMMANDER_RECONNECT {
        @Override
        public boolean validate(Object value) {
            return validateNumber(value, 1, Long.MAX_VALUE);
        }
    };

    public boolean validateNumber(Object value, long lower, long upper) {
        if (!List.of(Integer.class, Long.class).contains(value.getClass())) return false;

        long concreteValue = Long.parseLong(value.toString());
        return concreteValue >= lower && concreteValue <= upper;
    }

    public abstract boolean validate(Object value);
    public Object defaultValue;
}
