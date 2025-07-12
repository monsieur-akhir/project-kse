package com.assureplus.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "utilisateurpermission",
    uniqueConstraints = @UniqueConstraint(columnNames = {"utilisateur_id", "permission_id"})
)
public class UtilisateurPermission extends AuditableEntity implements Serializable {
    @EmbeddedId
    private UtilisateurPermissionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("utilisateurId")
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @Column(name = "actif", nullable = false)
    private boolean actif;

    @Column(name = "activate_at")
    private java.time.LocalDateTime activateAt;

    @Column(name = "activate_by")
    private String activateBy;
} 