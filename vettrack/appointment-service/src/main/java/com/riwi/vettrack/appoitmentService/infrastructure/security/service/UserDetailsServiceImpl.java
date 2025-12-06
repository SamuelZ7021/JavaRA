package com.riwi.vettrack.appoitmentService.infrastructure.security.service;

import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity.UserEntity;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserJpaRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Buscar usuario en BD
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 2. Convertir Rol a Authority de Spring (Debe empezar con "ROLE_")
        // Ejemplo: VETERINARIO -> ROLE_VETERINARIO
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().name());

        // 3. Retornar el objeto User de Spring Security
        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                List.of(authority) // Lista de permisos
        );
    }
}