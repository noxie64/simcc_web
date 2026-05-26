package at.simcc.simcc_backend.entities;

import at.simcc.simcc_backend.converter.Inet4AddressConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.net.Inet4Address;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Project: backend
 * Created by: Marko Kushlyk
 * Date: 12.05.2026
 * Time: 9:40
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "infected_ip")
public class InfectedIP {
    @Id
    private LocalDate since;
    @JdbcTypeCode(SqlTypes.INET)
    @Column(name = "ip", columnDefinition = "inet")
    @Convert(converter = Inet4AddressConverter.class)
    private Inet4Address ip;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "iid")
    private Infected infected;
}
