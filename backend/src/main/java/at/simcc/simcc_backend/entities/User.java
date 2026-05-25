package at.simcc.simcc_backend.entities;

import at.simcc.simcc_backend.entities.trojan_setting.TrojanSetting;
import at.simcc.simcc_backend.generator.CustomIdGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Project: simcc_web
 * Created by: Marko Kushlyk
 * Date: 27.03.2026
 * Time: 10:06
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    /**
     * @GenericGenerator -> is to specify an own IdGenerator and a name to it
     */

    @Id
    @GenericGenerator(name = "custom_generator", type = CustomIdGenerator.class)
    @GeneratedValue(generator = "custom_generator")
    private Long userId;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private String totpSecret;

    @OneToMany(mappedBy = "trojanId", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Trojan> trojans;

    /**
     * Is made for checking, if the account is meant as admin or user
     */
    @Transient
    @Builder.Default
    private Boolean isAdmin = false;
}
