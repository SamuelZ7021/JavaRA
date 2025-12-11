package com.coopcredit.creditapplication.application.service;

import com.coopcredit.creditapplication.application.dto.auth.AuthRequest;
import com.coopcredit.creditapplication.application.dto.auth.AuthResponse;
import com.coopcredit.creditapplication.application.dto.auth.RegisterRequest;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.entity.AffiliateEntity;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.entity.UserEntity;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.repository.AffiliateRepository;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.repository.UserRepository;
import com.coopcredit.creditapplication.infrastructure.config.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AffiliateRepository affiliateRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        // 1. Determinar el Rol (Lógica Dinámica)
        UserEntity.Role role = UserEntity.Role.ROLE_AFILIADO;

        if (request.getRole() != null && !request.getRole().isEmpty()) {
            try {
                role = UserEntity.Role.valueOf(request.getRole());
            } catch (IllegalArgumentException e) {
                System.out.println("Rol inválido recibido: " + request.getRole() + ". Asignando ROLE_AFILIADO.");
            }
        }

        // 2. Crear Usuario (Security)
        var user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        var savedUser = userRepository.save(user);


        var affiliate = AffiliateEntity.builder()
                .user(savedUser)
                .fullName(request.getFullName())
                .email(request.getEmail())
                .address(request.getAddress())
                .salary(request.getSalary())
                .active(true)
                .build();

        affiliateRepository.save(affiliate);

        // 4. Generar Token
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}