package com.assureplus.auth.dto;

import lombok.Data;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class UtilisateurCreateDTO {
    @NotBlank(message = "L'identifiant est obligatoire")
    private String identifiant;
    private String email;
    private String telephone;
    private String languePreferee;
    @NotNull(message = "Le rôle est obligatoire")
    private UUID roleId;
    private String password;
} 