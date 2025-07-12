package com.assureplus.auth.repository;

import com.assureplus.auth.entity.UtilisateurPermission;
import com.assureplus.auth.entity.UtilisateurPermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurPermissionRepository extends JpaRepository<UtilisateurPermission, UtilisateurPermissionId> {
} 