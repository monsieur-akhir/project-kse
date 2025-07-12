package com.assureplus.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorResponse", description = "Réponse d'erreur standard")
public class ErrorResponse {
    @Schema(description = "Message d'erreur", example = "Erreur détaillée")
    public String error;
    public ErrorResponse(String error) { this.error = error; }
    public ErrorResponse() {}
} 