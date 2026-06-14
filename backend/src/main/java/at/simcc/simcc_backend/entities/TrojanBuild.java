package at.simcc.simcc_backend.entities;

import at.simcc.simcc_backend.entities.trojan_setting.TrojanSetting;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 6/7/26
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrojanBuild {
    @Id
    private UUID buildId;

    @Column(nullable = false)
    private LocalDateTime buildAt;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @ToString.Exclude
    private Trojan trojan;
}
