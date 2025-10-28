package com.example1.demo.service;

import com.example1.demo.dtos.AuthResponseDTO;
import com.example1.demo.dtos.LoginDTO;
import com.example1.demo.dtos.RegisterDTO;
import com.example1.demo.exceptions.UserAlreadyExistsException;
import com.example1.demo.user.Role;
import com.example1.demo.module.User;
import com.example1.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Maneja la lógica de inicio de sesión de un usuario.
     */
    public AuthResponseDTO login(LoginDTO loginDTO) {
        // 1. Spring Security autentica al usuario.
        // Si las credenciales son incorrectas, lanzará una excepción.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );

        // 2. Si la autenticación fue exitosa, buscamos al usuario
        UserDetails user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado después de la autenticación"));

        // 3. Generamos el token JWT
        String token = jwtService.generateToken(user);

        // 4. Devolvemos el token
        return new AuthResponseDTO(token);
    }

    /**
     * Maneja la lógica de registro de un nuevo usuario.
     */
    public AuthResponseDTO register(RegisterDTO registerDTO) {
        // 1. Verificamos si el usuario ya existe
        if (userRepository.findByUsername(registerDTO.getUsername()).isPresent()) {
            // Sería bueno crear una excepción personalizada para esto
            throw new UserAlreadyExistsException("Error: El email '" + registerDTO.getUsername() + "' ya está registrado.");
        }

        // 2. Creamos el nuevo usuario
        User user = new User(
                registerDTO.getUsername(),
                passwordEncoder.encode(registerDTO.getPassword()), // ¡Codificamos la contraseña!
                Role.ADMIN  // Asignamos el rol USER por defecto
        );

        // 3. Guardamos el usuario en la base de datos
        userRepository.save(user);

        // 4. Generamos y devolvemos un token para el nuevo usuario
        String token = jwtService.generateToken(user);
        return new AuthResponseDTO(token);
    }
}
