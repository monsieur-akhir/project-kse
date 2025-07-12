package com.assureplus.auth.controller;

import com.assureplus.auth.dto.RoleCreateDTO;
import com.assureplus.auth.dto.RoleDTO;
import com.assureplus.auth.dto.UtilisateurDTO;
import com.assureplus.auth.service.RoleService;
import com.assureplus.auth.service.UtilisateurService;
import com.assureplus.auth.model.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Tag(name = "Rôles", description = "Gestion des rôles utilisateurs")
@RestController
@RequestMapping("/api/roles")
@SecurityRequirement(name = "bearerAuth")
public class RoleController {
    private static final Logger log = LoggerFactory.getLogger(RoleController.class);
    private final RoleService roleService;
    private final UtilisateurService utilisateurService;

    public RoleController(RoleService roleService, UtilisateurService utilisateurService) {
        this.roleService = roleService;
        this.utilisateurService = utilisateurService;
    }

    @Operation(summary = "Créer un rôle", description = "Crée un nouveau rôle utilisateur.",
        requestBody = @RequestBody(content = @Content(
            schema = @Schema(implementation = RoleCreateDTO.class),
            examples = @ExampleObject(value = "{\n  \"nom\": \"ADMIN\",\n  \"description\": \"Administrateur de la plateforme\"\n}")
        )))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rôle créé avec succès", content = @Content(schema = @Schema(implementation = RoleDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requête invalide ou rôle déjà existant", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @PreAuthorize("hasAuthority('PERM_ADMIN')")
    public ResponseEntity<RoleDTO> createRole(@org.springframework.web.bind.annotation.RequestBody RoleCreateDTO dto) {
        log.info("[API] POST /api/roles - Création d'un rôle : {}", dto.getNom());
        return ResponseEntity.ok(roleService.createRole(dto));
    }

    @Operation(summary = "Lister les rôles", description = "Retourne la liste de tous les rôles.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste des rôles", content = @Content(schema = @Schema(implementation = RoleDTO.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        log.info("[API] GET /api/roles - Liste des rôles");
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @Operation(summary = "Obtenir un rôle par ID", description = "Retourne le détail d'un rôle à partir de son identifiant.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Détail du rôle", content = @Content(schema = @Schema(implementation = RoleDTO.class))),
        @ApiResponse(responseCode = "404", description = "Rôle non trouvé", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable UUID id) {
        log.info("[API] GET /api/roles/{} - Détail du rôle", id);
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @Operation(summary = "Lister les utilisateurs d'un rôle", description = "Retourne la liste des utilisateurs associés à un rôle donné.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste des utilisateurs", content = @Content(schema = @Schema(implementation = UtilisateurDTO.class)))
    })
    @GetMapping("/{id}/utilisateurs")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<UtilisateurDTO>> getUtilisateursByRole(@PathVariable UUID id) {
        log.info("[API] GET /api/roles/{}/utilisateurs - Liste des utilisateurs du rôle", id);
        return ResponseEntity.ok(utilisateurService.getUtilisateursByRole(id));
    }
} 