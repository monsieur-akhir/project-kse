package com.assureplus.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import java.util.UUID;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "utilisateur")
public class Utilisateur extends AuditableEntity {
    @Id
    @Column(name = "utilisateur_id")
    private UUID id;

    @Column(name = "actif", nullable = false)
    private boolean actif;

    @Column(name = "activate_at")
    private LocalDateTime activateAt;

    @Column(name = "activate_by")
    private String activateBy;

    @Column(name = "identifiant", nullable = false, unique = true, length = 64)
    private String identifiant;

    @Column(name = "est_verifie", nullable = false)
    private boolean estVerifie;

    @Column(name = "email", unique = true, length = 256)
    private String email;

    @Column(name = "telephone", unique = true, length = 64)
    private String telephone;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "langue_preferee", length = 10)
    private LanguePreferee languePreferee;

    @Column(name = "date_derniere_connexion")
    private LocalDateTime dateDerniereConnexion;

    @Column(name = "origine_connexion", length = 255)
    private String origineConnexion;

    @Column(name = "last_login_ip", length = 255)
    private String lastLoginIp;

    @Column(name = "canal_par_defaut", length = 255)
    private String canalParDefaut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "utilisateurpermission",
        joinColumns = @JoinColumn(name = "utilisateur_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;
} 