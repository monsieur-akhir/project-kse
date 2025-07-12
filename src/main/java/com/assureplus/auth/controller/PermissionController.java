package com.assureplus.auth.controller;

import com.assureplus.auth.dto.PermissionCreateDTO;
import com.assureplus.auth.dto.PermissionDTO;
import com.assureplus.auth.service.PermissionService;
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

@Tag(name = "Permissions", description = "Gestion des permissions utilisateurs")
@RestController
@RequestMapping("/api/permissions")
@SecurityRequirement(name = "Bearer Authentication")
public class PermissionController {
    private final PermissionService permissionService;
    private static final Logger log = LoggerFactory.getLogger(PermissionController.class);

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Operation(summary = "Créer une permission", description = "Crée une nouvelle permission.",
        requestBody = @RequestBody(content = @Content(
            schema = @Schema(implementation = PermissionCreateDTO.class),
            examples = @ExampleObject(value = "{\n  \"code\": \"PERM_CREATE_USER\",\n  \"description\": \"Créer un utilisateur\",\n  \"parentId\": null\n}")
        )))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Permission créée avec succès", content = @Content(schema = @Schema(implementation = PermissionDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requête invalide ou permission déjà existante", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @PreAuthorize("hasAuthority('PERM_ADMIN')")
    public ResponseEntity<PermissionDTO> createPermission(@org.springframework.web.bind.annotation.RequestBody PermissionCreateDTO dto) {
        log.info("[API] POST /api/permissions - Création d'une permission : {}", dto.getCode());
        return ResponseEntity.ok(permissionService.createPermission(dto));
    }

    @Operation(summary = "Lister les permissions", description = "Retourne la liste de toutes les permissions.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste des permissions", content = @Content(schema = @Schema(implementation = PermissionDTO.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        log.info("[API] GET /api/permissions - Liste des permissions");
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @Operation(summary = "Obtenir une permission par ID", description = "Retourne le détail d'une permission à partir de son identifiant.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Détail de la permission", content = @Content(schema = @Schema(implementation = PermissionDTO.class))),
        @ApiResponse(responseCode = "404", description = "Permission non trouvée", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable UUID id) {
        log.info("[API] GET /api/permissions/{} - Détail de la permission", id);
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }
} 