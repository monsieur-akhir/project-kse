package com.assureplus.auth.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class RoleDTO {
    private UUID id;
    private String nom;
    private String description;
    private boolean actif;
} 