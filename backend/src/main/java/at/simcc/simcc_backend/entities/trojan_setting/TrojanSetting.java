package at.simcc.simcc_backend.entities.trojan_setting;

import at.simcc.simcc_backend.entities.Trojan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Struct;
import org.hibernate.type.SqlTypes;

/**
 * Project: simcc_backend
 * Created by: Georg Kollegger
 * Date: 5/25/26
 */
@Entity
@Data
@IdClass(TrojanSettingID.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrojanSetting {
    @Id
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "trojan_id")
    private Trojan trojan;
    @Id
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private TrojanSettingKey key;
    private String value;
}
