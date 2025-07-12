package com.assureplus.auth.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class PermissionDTO {
    private UUID id;
    private String code;
    private String description;
    private UUID parentId;
    private boolean actif;
} 