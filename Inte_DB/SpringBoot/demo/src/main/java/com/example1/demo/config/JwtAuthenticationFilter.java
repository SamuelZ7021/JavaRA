package com.example1.demo.config;

import com.example1.demo.service.JwtService; // Asegúrate que la ruta a tu JwtService sea correcta
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Le decimos a Spring que esta clase es un Bean
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService; // El bean que creamos en SecurityConfig

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Obtenemos el token de la cabecera "Authorization"
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Si no hay cabecera o no empieza con "Bearer ", pasamos al siguiente filtro
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraemos el token (quitando el "Bearer ")
        jwt = authHeader.substring(7);

        // 3. Extraemos el username del token (necesitamos añadir este método a JwtService)
        username = jwtService.extractUsername(jwt);

        // 4. Si tenemos username y el usuario NO está autenticado en el contexto de seguridad
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Cargamos los detalles del usuario desde la base de datos
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 5. Validamos el token
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Si el token es válido, creamos la autenticación
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // No se necesitan credenciales (contraseña)
                        userDetails.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 6. Establecemos la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Pasamos la petición al siguiente filtro de la cadena
        filterChain.doFilter(request, response);
    }
}