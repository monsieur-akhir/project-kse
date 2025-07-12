package com.assureplus.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "permission")
public class Permission extends AuditableEntity {
    @Id
    @Column(name = "permission_id")
    private UUID id;

    @Column(name = "actif", nullable = false)
    private boolean actif;

    @Column(name = "activate_at")
    private java.time.LocalDateTime activateAt;

    @Column(name = "activate_by")
    private String activateBy;

    @Column(name = "code", nullable = false, length = 64)
    private String code;

    @Column(name = "description", length = 256)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Permission parent;
} 