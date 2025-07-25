package com.assureplus.auth.repository;

import com.assureplus.auth.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
} 