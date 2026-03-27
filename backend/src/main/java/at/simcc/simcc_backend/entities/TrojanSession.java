package at.simcc.simcc_backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.Instant;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 3/27/26
 */
@Entity
public class TrojanSession {
    @Id
    @GeneratedValue
    private Integer trojanId;

    @Column(nullable = false, unique = true)
    private String ccid;

    @Column(nullable = false)
    private Instant createdAt;


}
