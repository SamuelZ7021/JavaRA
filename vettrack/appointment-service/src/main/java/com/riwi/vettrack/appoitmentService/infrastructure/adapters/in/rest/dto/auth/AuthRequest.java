package com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank String username,
        @NotBlank String password
) {}