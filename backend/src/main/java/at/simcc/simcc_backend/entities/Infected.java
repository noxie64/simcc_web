package at.simcc.simcc_backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

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
    private UUID iid;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private OSType osType;
    private String osSubType;
    private String osInfo;

    @OneToMany(mappedBy = "infected",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private List<InfectedIP> infectedIPS;
}
