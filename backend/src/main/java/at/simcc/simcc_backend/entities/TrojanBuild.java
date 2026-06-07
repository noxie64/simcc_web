package at.simcc.simcc_backend.entities;

import at.simcc.simcc_backend.entities.trojan_setting.TrojanSetting;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Trojan trojan;
}
