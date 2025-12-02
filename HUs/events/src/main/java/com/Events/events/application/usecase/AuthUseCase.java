package com.Events.events.application.usecase;

import com.Events.events.infrastructure.adapters.in.web.dto.resquest.LoginRequest;
import com.Events.events.infrastructure.adapters.in.web.dto.resquest.RegisterRequest;
import com.Events.events.infrastructure.adapters.in.web.dto.response.AuthResponse;
import com.Events.events.infrastructure.adapters.out.jpa.entity.UserEntity;
import com.Events.events.infrastructure.adapters.out.jpa.repository.SpringDataUserRepository;
import com.Events.events.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUseCase {

    private final SpringDataUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        var user = new UserEntity();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER"); // Rol por defecto

        repository.save(user);

        // Adaptamos nuestra entidad a UserDetails de Spring Security para generar el token
        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();

        var jwtToken = jwtService.generateToken(userDetails);
        return AuthResponse.builder().token(jwtToken).build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();

        var jwtToken = jwtService.generateToken(userDetails);
        return AuthResponse.builder().token(jwtToken).build();
    }
}