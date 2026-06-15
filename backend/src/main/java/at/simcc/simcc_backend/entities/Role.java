package at.simcc.simcc_backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 5/29/26
 */
@Entity
@Data
public class Role {
    @Id
    @GeneratedValue
    private Long roleId;

    @Column(nullable = false)
    private String displayName;
}
