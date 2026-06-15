package at.simcc.simcc_backend.entities;

import jakarta.persistence.*;
import lombok.*;

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


    private String osType;
    private String osVersion;
    private String osEdition;
    private String osCodeName;
    private String osBits;
    private String osArch;


    @ManyToOne( cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "trojan")
    @ToString.Exclude
    private Trojan trojan;

    @OneToMany(mappedBy = "infected",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private List<InfectedIP> infectedIPS;

    @Transient
    private boolean online;
}
