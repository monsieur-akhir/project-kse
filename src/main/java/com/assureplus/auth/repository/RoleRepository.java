package com.assureplus.auth.repository;

import com.assureplus.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
} 