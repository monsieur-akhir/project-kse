package com.assureplus.auth.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class UtilisateurPermissionDTO {
    private UUID utilisateurId;
    private UUID permissionId;
    private boolean actif;
} 