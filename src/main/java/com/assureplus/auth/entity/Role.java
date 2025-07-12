package com.assureplus.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "role")
public class Role extends AuditableEntity {
    @Id
    @Column(name = "role_id")
    private UUID id;

    @Column(name = "actif", nullable = false)
    private boolean actif;

    @Column(name = "activate_at")
    private java.time.LocalDateTime activateAt;

    @Column(name = "activate_by")
    private String activateBy;

    @Column(name = "nom", nullable = false, length = 64)
    private String nom;

    @Column(name = "description", length = 256)
    private String description;
} 