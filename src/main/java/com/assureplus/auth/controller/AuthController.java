package com.assureplus.auth.controller;

import com.assureplus.auth.security.JwtTokenProvider;
import com.assureplus.auth.security.CustomUserDetailsService;
import com.assureplus.auth.entity.Utilisateur;
import com.assureplus.auth.entity.Permission;
import com.assureplus.auth.repository.UtilisateurRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.assureplus.auth.model.ErrorResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final UtilisateurRepository utilisateurRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, CustomUserDetailsService userDetailsService, UtilisateurRepository utilisateurRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Operation(
        summary = "Authentification utilisateur (login)",
        description = "Permet à un utilisateur de s'authentifier et d'obtenir un JWT. Le token doit ensuite être utilisé dans le header Authorization pour accéder aux autres endpoints sécurisés.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Authentification réussie, JWT retourné",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\n  \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"\n}"),
                schema = @Schema(implementation = Map.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Identifiant ou mot de passe invalide",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\n  \"error\": \"Identifiant ou mot de passe invalide\"\n}"),
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
        @RequestBody(
            description = "Identifiants de connexion (identifiant et mot de passe)",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\n  \"identifiant\": \"root\",\n  \"password\": \"votre_mot_de_passe\"\n}")
            )
        )
        @org.springframework.web.bind.annotation.RequestBody Map<String, String> loginRequest
    ) {
        String identifiant = loginRequest.get("identifiant");
        log.info("[API] POST /api/auth/login - Tentative de login pour : {}", identifiant);
        try {
            String password = loginRequest.get("password");
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(identifiant, password)
            );
            Utilisateur utilisateur = utilisateurRepository.findAll().stream()
                    .filter(u -> u.getIdentifiant().equals(identifiant))
                    .findFirst()
                    .orElseThrow();
            String role = utilisateur.getRole() != null ? utilisateur.getRole().getNom() : null;
            List<String> permissions = utilisateur.getPermissions() != null ?
                    utilisateur.getPermissions().stream().map(Permission::getCode).collect(Collectors.toList()) : List.of();
            String token = jwtTokenProvider.generateToken(identifiant, utilisateur.getId(), role, permissions);
            log.info("[API] Authentification réussie pour : {} (role: {})", identifiant, role);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (AuthenticationException e) {
            log.warn("[API] Authentification échouée pour : {}", identifiant);
            return ResponseEntity.status(401).body(Map.of("error", "Identifiant ou mot de passe invalide"));
        }
    }
} 