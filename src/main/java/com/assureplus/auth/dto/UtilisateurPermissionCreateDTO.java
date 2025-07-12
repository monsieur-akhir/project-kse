package com.assureplus.auth.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class UtilisateurPermissionCreateDTO {
    private UUID utilisateurId;
    private UUID permissionId;
} 