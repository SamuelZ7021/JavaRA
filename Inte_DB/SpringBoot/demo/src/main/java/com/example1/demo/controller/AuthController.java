package com.example1.demo.controller;

import com.example1.demo.dtos.AuthResponseDTO;
import com.example1.demo.dtos.LoginDTO;
import com.example1.demo.dtos.RegisterDTO;
import com.example1.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth") // Esta es la ruta pública que definimos en SecurityConfig
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        // El servicio maneja la autenticación y devuelve el token
        AuthResponseDTO response = authService.login(loginDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterDTO registerDTO) {
        // El servicio maneja el registro y devuelve el token
        AuthResponseDTO response = authService.register(registerDTO);
        return ResponseEntity.ok(response);
    }
}
