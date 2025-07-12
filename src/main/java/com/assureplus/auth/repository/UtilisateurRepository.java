package com.assureplus.auth.repository;

import com.assureplus.auth.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {
    List<Utilisateur> findByRoleId(UUID roleId);
} 