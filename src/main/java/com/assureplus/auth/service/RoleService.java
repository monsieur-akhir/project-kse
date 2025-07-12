package com.assureplus.auth.service;

import com.assureplus.auth.dto.RoleCreateDTO;
import com.assureplus.auth.dto.RoleDTO;
import com.assureplus.auth.entity.Role;
import com.assureplus.auth.mapper.RoleMapper;
import com.assureplus.auth.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private static final Logger log = LoggerFactory.getLogger(RoleService.class);

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Transactional
    public RoleDTO createRole(RoleCreateDTO dto) {
        log.info("Création d'un rôle : {}", dto.getNom());
        Role role = roleMapper.toEntity(dto);
        role.setId(UUID.randomUUID());
        role.setActif(true);
        role.setCreatedAt(java.time.LocalDateTime.now());
        role = roleRepository.save(role);
        return roleMapper.toDTO(role);
    }

    @Transactional(readOnly = true)
    public List<RoleDTO> getAllRoles() {
        log.info("Récupération de tous les rôles");
        return roleRepository.findAll().stream()
                .map(roleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoleDTO getRoleById(UUID id) {
        log.info("Récupération du rôle par id : {}", id);
        return roleRepository.findById(id)
                .map(roleMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
    }
} 