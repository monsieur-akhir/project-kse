package com.assureplus.auth.service;

import com.assureplus.auth.dto.UtilisateurCreateDTO;
import com.assureplus.auth.dto.UtilisateurDTO;
import com.assureplus.auth.dto.PermissionDTO;
import com.assureplus.auth.entity.Utilisateur;
import com.assureplus.auth.entity.Permission;
import com.assureplus.auth.entity.Role;
import com.assureplus.auth.mapper.UtilisateurMapper;
import com.assureplus.auth.mapper.PermissionMapper;
import com.assureplus.auth.repository.UtilisateurRepository;
import com.assureplus.auth.repository.RoleRepository;
import com.assureplus.auth.repository.PermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UtilisateurMapper utilisateurMapper;
    private final PermissionMapper permissionMapper;
    private static final Logger log = LoggerFactory.getLogger(UtilisateurService.class);

    public UtilisateurService(UtilisateurRepository utilisateurRepository, RoleRepository roleRepository, PermissionRepository permissionRepository, UtilisateurMapper utilisateurMapper, PermissionMapper permissionMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.utilisateurMapper = utilisateurMapper;
        this.permissionMapper = permissionMapper;
    }

    @Transactional
    public UtilisateurDTO createUtilisateur(UtilisateurCreateDTO dto) {
        log.info("Création d'un utilisateur : {}", dto.getIdentifiant());
        Utilisateur utilisateur = utilisateurMapper.toEntity(dto);
        utilisateur.setId(UUID.randomUUID());
        utilisateur.setActif(true);
        utilisateur.setEstVerifie(false);
        utilisateur.setCreatedAt(java.time.LocalDateTime.now());
        utilisateur.setRole(roleRepository.findById(dto.getRoleId()).orElseThrow(() -> new RuntimeException("Rôle non trouvé")));
        // Mot de passe par défaut si non fourni
        String motDePasse = (dto.getPassword() == null || dto.getPassword().isBlank()) ? "kse123#" : dto.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        utilisateur.setPassword(encoder.encode(motDePasse));
        utilisateur = utilisateurRepository.save(utilisateur);
        // Attribuer la permission PERM_USER par défaut
        Permission permUser = permissionRepository.findAll().stream()
            .filter(p -> "PERM_USER".equals(p.getCode()))
            .findFirst()
            .orElse(null);
        if (permUser != null) {
            utilisateur.setPermissions(Set.of(permUser));
            utilisateur = utilisateurRepository.save(utilisateur);
        }
        return utilisateurMapper.toDTO(utilisateur);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "utilisateurs", key = "#utilisateurId")
    public UtilisateurDTO getUtilisateurWithPermissions(UUID utilisateurId) {
        log.info("Récupération de l'utilisateur avec permissions, id : {}", utilisateurId);
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        UtilisateurDTO dto = utilisateurMapper.toDTO(utilisateur);
        Set<PermissionDTO> permissions = utilisateur.getPermissions().stream()
                .map(permissionMapper::toDTO)
                .collect(Collectors.toSet());
        dto.setPermissions(permissions);
        return dto;
    }

    @Transactional(readOnly = true)
    public List<UtilisateurDTO> getUtilisateursByRole(UUID roleId) {
        log.info("Récupération des utilisateurs par rôle, roleId : {}", roleId);
        return utilisateurRepository.findByRoleId(roleId).stream()
                .map(utilisateurMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UtilisateurDTO assignPermissionsToUtilisateur(UUID utilisateurId, Set<UUID> permissionIds) {
        log.info("Association de permissions à l'utilisateur id : {}", utilisateurId);
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(permissionIds));
        utilisateur.setPermissions(permissions);
        utilisateur = utilisateurRepository.save(utilisateur);
        UtilisateurDTO dto = utilisateurMapper.toDTO(utilisateur);
        Set<PermissionDTO> permissionDTOs = utilisateur.getPermissions().stream()
                .map(permissionMapper::toDTO)
                .collect(Collectors.toSet());
        dto.setPermissions(permissionDTOs);
        return dto;
    }

    public UtilisateurDTO blockUtilisateur(UUID utilisateurId) {
        log.info("Blocage de l'utilisateur id : {}", utilisateurId);
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        utilisateur.setActif(false);
        utilisateur = utilisateurRepository.save(utilisateur);
        return utilisateurMapper.toDTO(utilisateur);
    }

    public UtilisateurDTO unblockUtilisateur(UUID utilisateurId) {
        log.info("Déblocage de l'utilisateur id : {}", utilisateurId);
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        utilisateur.setActif(true);
        utilisateur = utilisateurRepository.save(utilisateur);
        return utilisateurMapper.toDTO(utilisateur);
    }
} 