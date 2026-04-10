package at.simcc.simcc_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/10/26
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Infected {
    @Id
    @GeneratedValue
    private Long iid;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private OSType osType;
    private String osSubType;
    private String osInfo;
}
