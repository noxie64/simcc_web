package at.simcc.simcc_backend.entities;

import at.simcc.simcc_backend.entities.trojan_setting.TrojanSetting;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 3/27/26
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Trojan {
    @Id
    @GeneratedValue
    private Long trojanId;

    @Column(nullable = false, unique = true)
    private String ccid;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "trojan", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<TrojanSetting> trojanSettings;
}
