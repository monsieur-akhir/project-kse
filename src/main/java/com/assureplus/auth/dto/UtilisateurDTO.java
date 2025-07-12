package com.assureplus.auth.dto;

import lombok.Data;
import java.util.Set;
import java.util.UUID;

@Data
public class UtilisateurDTO {
    private UUID id;
    private String identifiant;
    private String email;
    private String telephone;
    private String languePreferee;
    private boolean actif;
    private UUID roleId;
    private Set<PermissionDTO> permissions;
} 