package com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest.dto.auth;

import com.riwi.vettrack.appoitmentService.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotBlank(message = "Username/Email is required")
        @Email(message = "Invalid email format")
        String username,

        @NotBlank(message = "Password is required")
        String password,

        @NotNull(message = "Role is required")
        Role role
) {}