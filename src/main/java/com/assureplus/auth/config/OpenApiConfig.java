package com.assureplus.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("ASSURE-PLUS Auth Service API")
                        .version("1.0")
                        .description("API sécurisée de gestion des utilisateurs, rôles et permissions pour ASSURE-PLUS.\n\nToutes les routes (sauf /api/auth/login) nécessitent un JWT Bearer valide.\n\n**Exemple d'en-tête Authorization :** `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
} 