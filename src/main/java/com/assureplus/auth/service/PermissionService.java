package com.assureplus.auth.service;

import com.assureplus.auth.dto.PermissionCreateDTO;
import com.assureplus.auth.dto.PermissionDTO;
import com.assureplus.auth.entity.Permission;
import com.assureplus.auth.mapper.PermissionMapper;
import com.assureplus.auth.repository.PermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;
    private static final Logger log = LoggerFactory.getLogger(PermissionService.class);

    public PermissionService(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    @Transactional
    public PermissionDTO createPermission(PermissionCreateDTO dto) {
        log.info("Création d'une permission : {}", dto.getCode());
        Permission permission = permissionMapper.toEntity(dto);
        permission.setId(UUID.randomUUID());
        permission.setActif(true);
        permission.setCreatedAt(java.time.LocalDateTime.now());
        if (dto.getParentId() != null) {
            permission.setParent(permissionRepository.findById(dto.getParentId()).orElse(null));
        }
        permission = permissionRepository.save(permission);
        return permissionMapper.toDTO(permission);
    }

    @Transactional(readOnly = true)
    public List<PermissionDTO> getAllPermissions() {
        log.info("Récupération de toutes les permissions");
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PermissionDTO getPermissionById(UUID id) {
        log.info("Récupération de la permission par id : {}", id);
        return permissionRepository.findById(id)
                .map(permissionMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Permission non trouvée"));
    }
} 