package com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest;

import com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest.dto.auth.AuthRequest;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest.dto.auth.AuthResponse;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest.dto.auth.RegisterRequest;
import com.riwi.vettrack.appoitmentService.infrastructure.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}