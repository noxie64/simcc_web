package at.simcc.simcc_backend.entities.trojan_setting;

import at.simcc.simcc_backend.entities.Trojan;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.IdClass;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;

/**
 * Project: simcc_backend
 * Created by: Georg Kollegger
 * Date: 5/25/26
 */
public class TrojanSettingID implements Serializable {
    private Trojan trojan;
    private TrojanSettingKey key;
}
