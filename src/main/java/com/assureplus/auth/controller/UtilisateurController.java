package com.assureplus.auth.controller;

import com.assureplus.auth.dto.UtilisateurCreateDTO;
import com.assureplus.auth.dto.UtilisateurDTO;
import com.assureplus.auth.service.UtilisateurService;
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

import jakarta.validation.Valid;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.assureplus.auth.model.ErrorResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Tag(name = "Utilisateurs", description = "Gestion des utilisateurs de la plateforme")
@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {
    private static final Logger log = LoggerFactory.getLogger(UtilisateurController.class);
    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @Operation(
        summary = "Créer un utilisateur",
        description = "Crée un nouvel utilisateur dans la plateforme. Accessible uniquement aux administrateurs.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès",
            content = @Content(schema = @Schema(implementation = UtilisateurDTO.class),
                examples = @ExampleObject(value = "{\n  \"id\": \"uuid\",\n  \"identifiant\": \"jdupont\",\n  \"email\": \"j.dupont@email.com\",\n  \"telephone\": \"0601020304\",\n  \"languePreferee\": \"FRANCAIS\",\n  \"actif\": true,\n  \"roleId\": \"uuid-role\",\n  \"permissions\": []\n}"))),
        @ApiResponse(responseCode = "400", description = "Requête invalide ou utilisateur déjà existant",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = "{ \"error\": \"L'email existe déjà\" }"))),
        @ApiResponse(responseCode = "401", description = "Non authentifié (JWT manquant ou invalide)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Accès refusé (rôle insuffisant)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestBody(
        description = "Données du nouvel utilisateur (le mot de passe est obligatoire)",
        required = true,
        content = @Content(
            schema = @Schema(implementation = UtilisateurCreateDTO.class),
            examples = @ExampleObject(value = "{\n  \"identifiant\": \"jdupont\",\n  \"email\": \"j.dupont@email.com\",\n  \"telephone\": \"0601020304\",\n  \"languePreferee\": \"FRANCAIS\",\n  \"roleId\": \"uuid-du-role\",\n  \"password\": \"MonSuperMotDePasse123\"\n}")
        )
    )
    @PostMapping
    @PreAuthorize("hasAuthority('PERM_ADMIN')")
    public ResponseEntity<?> createUtilisateur(@org.springframework.web.bind.annotation.RequestBody UtilisateurCreateDTO dto) {
        log.info("[API] POST /api/utilisateurs - Création d'un utilisateur : {}", dto.getIdentifiant());
        try {
            UtilisateurDTO utilisateur = utilisateurService.createUtilisateur(dto);
            return ResponseEntity.ok(utilisateur);
        } catch (Exception e) {
            log.error("Erreur lors de la création de l'utilisateur : {}", e.getMessage(), e);
            return ResponseEntity.status(400).body(java.util.Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Obtenir un utilisateur avec ses permissions", description = "Retourne le détail d'un utilisateur et ses permissions.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Détail de l'utilisateur", content = @Content(schema = @Schema(implementation = UtilisateurDTO.class))),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or hasAuthority('PERM_VIEW_USER')")
    public ResponseEntity<UtilisateurDTO> getUtilisateurWithPermissions(@PathVariable UUID id) {
        log.info("[API] GET /api/utilisateurs/{} - Détail utilisateur avec permissions", id);
        return ResponseEntity.ok(utilisateurService.getUtilisateurWithPermissions(id));
    }

    @Operation(summary = "Associer des permissions à un utilisateur", description = "Associe une ou plusieurs permissions à un utilisateur.",
        requestBody = @RequestBody(content = @Content(
            schema = @Schema(implementation = Set.class),
            examples = @ExampleObject(value = "[\n  \"uuid-permission-1\",\n  \"uuid-permission-2\"\n]")
        )))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Permissions associées avec succès", content = @Content(schema = @Schema(implementation = UtilisateurDTO.class))),
        @ApiResponse(responseCode = "404", description = "Utilisateur ou permission non trouvé", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{id}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UtilisateurDTO> assignPermissionsToUtilisateur(@PathVariable UUID id, @org.springframework.web.bind.annotation.RequestBody Set<UUID> permissionIds) {
        log.info("[API] POST /api/utilisateurs/{}/permissions - Association de permissions : {}", id, permissionIds);
        return ResponseEntity.ok(utilisateurService.assignPermissionsToUtilisateur(id, permissionIds));
    }

    @Operation(summary = "Bloquer un utilisateur", description = "Bloque un utilisateur (désactive son accès à la plateforme).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utilisateur bloqué avec succès", content = @Content(schema = @Schema(implementation = UtilisateurDTO.class))),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UtilisateurDTO> blockUtilisateur(@PathVariable UUID id) {
        log.info("[API] PATCH /api/utilisateurs/{}/block - Blocage utilisateur", id);
        return ResponseEntity.ok(utilisateurService.blockUtilisateur(id));
    }

    @Operation(summary = "Débloquer un utilisateur", description = "Débloque un utilisateur (réactive son accès à la plateforme).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utilisateur débloqué avec succès", content = @Content(schema = @Schema(implementation = UtilisateurDTO.class))),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/unblock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UtilisateurDTO> unblockUtilisateur(@PathVariable UUID id) {
        log.info("[API] PATCH /api/utilisateurs/{}/unblock - Déblocage utilisateur", id);
        return ResponseEntity.ok(utilisateurService.unblockUtilisateur(id));
    }
} 