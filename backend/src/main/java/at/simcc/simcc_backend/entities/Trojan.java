package at.simcc.simcc_backend.entities;

import at.simcc.simcc_backend.entities.trojan_setting.TrojanSetting;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 3/27/26
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Trojan {
    @Id
    @GeneratedValue
    private UUID ccid;

    @Column(nullable = false, length = 64)
    private String name;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Transient
    private boolean building = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "trojan", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<TrojanSetting> trojanSettings;

    @OneToMany(mappedBy = "trojan", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<TrojanBuild> trojanBuilds;

    @OneToMany(mappedBy = "trojan", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Infected> infectends;
}
