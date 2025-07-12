package com.assureplus.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@Setter
public class UtilisateurPermissionId implements Serializable {
    @Column(name = "utilisateur_id")
    private UUID utilisateurId;

    @Column(name = "permission_id")
    private UUID permissionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UtilisateurPermissionId that = (UtilisateurPermissionId) o;
        return Objects.equals(utilisateurId, that.utilisateurId) && Objects.equals(permissionId, that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(utilisateurId, permissionId);
    }
} 