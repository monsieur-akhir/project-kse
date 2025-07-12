package com.assureplus.auth.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class RoleCreateDTO {
    @NotBlank(message = "Le nom du rôle est obligatoire")
    private String nom;
    private String description;
} 