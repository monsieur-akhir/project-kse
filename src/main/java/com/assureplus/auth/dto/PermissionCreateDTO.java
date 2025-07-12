package com.assureplus.auth.dto;

import lombok.Data;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;

@Data
public class PermissionCreateDTO {
    @NotBlank(message = "Le code de la permission est obligatoire")
    private String code;
    private String description;
    private UUID parentId;
} 