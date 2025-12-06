package com.riwi.vettrack.appoitmentService.infrastructure.security.service;

import com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest.dto.auth.AuthRequest;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest.dto.auth.AuthResponse;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest.dto.auth.RegisterRequest;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity.UserEntity;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.repository.UserJpaRepository;
import com.riwi.vettrack.appoitmentService.infrastructure.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService; // Para recargar el usuario completo al login

    public AuthResponse register(RegisterRequest request) {
        // 1. Crear Usuario
        var user = UserEntity.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password())) // ¡Encriptar siempre!
                .role(request.role())
                .build();

        // 2. Guardar en BD
        userRepository.save(user);

        // 3. Generar Token inmediatamente para hacer "Auto-Login"
        // Convertimos nuestra entidad a UserDetails
        var userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        var jwtToken = jwtService.generateToken(userDetails);

        return new AuthResponse(jwtToken);
    }

    public AuthResponse login(AuthRequest request) {
        // 1. Autenticar con Spring Security (Esto valida la contraseña automáticamente)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        // 2. Si pasa la autenticación, buscamos al usuario para generar el token
        var userDetails = userDetailsService.loadUserByUsername(request.username());
        var jwtToken = jwtService.generateToken(userDetails);

        return new AuthResponse(jwtToken);
    }
}