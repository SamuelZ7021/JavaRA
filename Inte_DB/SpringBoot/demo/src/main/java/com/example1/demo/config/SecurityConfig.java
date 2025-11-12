package com.example1.demo.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Necesario para la Autenticacion de roles (RBAC)
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    // BEAN 1: Filtro de seguridad principal
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitamos CSRF porque usaremos JWT (stateless)
                .csrf(csrf -> csrf.disable())

                // --- AÑADE ESTA LÍNEA AQUÍ ---
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Definimos las reglas de autorización
                .authorizeHttpRequests(auth -> auth
                        // Permitimos el acceso público a nuestros futuros endpoints de autenticación
                        .requestMatchers("/api/auth/**", "/h2-console/**", "/view/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products", "/api/products/**").authenticated()

                        // Todas las demás peticiones (incluyendo /api/products) deben ser autenticadas
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))

                // Le decimos a Spring Security que NO maneje sesiones (somos STATELLESS)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Definimos el proveedor de autenticación que creamos abajo
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // BEAN 2: Proveedor de autenticación
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenProvider = new DaoAuthenticationProvider();
        authenProvider.setUserDetailsService(userDetailsService); // busqueda del user
        authenProvider.setPasswordEncoder(passwordEncoder()); // como verificar la contraseña
        return authenProvider;
    }

    // BEAN 3: Servicio de autenticación
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // BEAN 4: Codificador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // ... (justo después o antes de tu passwordEncoder() bean)

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite peticiones desde tu front-end de Angular
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        // Permite los métodos HTTP que usarás
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Permite cabeceras comunes y la de 'Authorization' para tu JWT
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        // Permite que el navegador envíe credenciales (como cookies o tokens)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuración a todas las rutas de tu API
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
