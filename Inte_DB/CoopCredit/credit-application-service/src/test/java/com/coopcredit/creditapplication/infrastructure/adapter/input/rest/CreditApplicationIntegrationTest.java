package com.coopcredit.creditapplication.infrastructure.adapter.input.rest;

import com.coopcredit.creditapplication.application.dto.credit.CreditRequest;
import com.coopcredit.creditapplication.domain.model.RiskEvaluation;
import com.coopcredit.creditapplication.domain.ports.output.RiskEvaluationPort;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.entity.AffiliateEntity;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.entity.CreditApplicationEntity;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.entity.UserEntity;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.repository.AffiliateRepository;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.repository.CreditApplicationRepository;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.repository.UserRepository;
import com.coopcredit.creditapplication.infrastructure.config.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Importante: Revierte los cambios en DB después de cada test
class CreditApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AffiliateRepository affiliateRepository;

    @Autowired
    private CreditApplicationRepository creditApplicationRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean // Simulamos el servicio externo, no queremos hacer llamadas HTTP reales aquí
    private RiskEvaluationPort riskEvaluationPort;

    private String affiliateToken;
    private String adminToken;

    @BeforeEach
    void setUp() {
        // 1. Crear Usuario y Afiliado (ROLE_AFILIADO)
        UserEntity userAffiliate = UserEntity.builder()
                .username("juan.perez")
                .password(passwordEncoder.encode("12345678"))
                .role(UserEntity.Role.ROLE_AFILIADO)
                .build();
        userRepository.save(userAffiliate);

        AffiliateEntity affiliate = AffiliateEntity.builder()
                .user(userAffiliate)
                .fullName("Juan Perez")
                .email("juan@test.com")
                .address("Calle Falsa 123")
                .salary(new BigDecimal("5000000"))
                .active(true)
                .build();
        affiliateRepository.save(affiliate);

        // Generar Token real para el test
        affiliateToken = "Bearer " + jwtService.generateToken(userAffiliate);

        // 2. Crear Usuario Admin (ROLE_ADMIN) para pruebas de consulta
        UserEntity userAdmin = UserEntity.builder()
                .username("admin.general")
                .password(passwordEncoder.encode("admin123"))
                .role(UserEntity.Role.ROLE_ADMIN)
                .build();
        userRepository.save(userAdmin);

        adminToken = "Bearer " + jwtService.generateToken(userAdmin);
    }

    // --- PRUEBA 1: Flujo completo de Creación de Crédito (Integration + Security) ---
    @Test
    void shouldCreateCreditApplicationSuccessfully() throws Exception {
        // GIVEN
        CreditRequest request = new CreditRequest();
        request.setAmount(new BigDecimal("150000"));
        request.setTermMonths(12);

        // Mock del servicio externo (Riesgo bajo = Aprobado)
        when(riskEvaluationPort.evaluateRisk(anyString()))
                .thenReturn(RiskEvaluation.builder().score(90).build());

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/credits")
                        .header("Authorization", affiliateToken) // Auth header
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.amount").value(150000))
                .andExpect(jsonPath("$.id").exists()); // Verifica que se generó ID
    }

    // --- PRUEBA 2: Validación de Seguridad (Sin Token) ---
    @Test
    void shouldForbidRequestWithoutToken() throws Exception {
        CreditRequest request = new CreditRequest();
        request.setAmount(new BigDecimal("150000"));
        request.setTermMonths(12);

        mockMvc.perform(post("/api/v1/credits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden()); // 403 Forbidden (o 401 según config)
    }

    // --- PRUEBA 3: Validación de Negocio (Monto Inválido) ---
    @Test
    void shouldReturnBadRequestForInvalidAmount() throws Exception {
        CreditRequest request = new CreditRequest();
        request.setAmount(new BigDecimal("-500")); // Monto negativo
        request.setTermMonths(12);

        mockMvc.perform(post("/api/v1/credits")
                        .header("Authorization", affiliateToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()) // Valida @Valid y GlobalControllerAdvice
                .andExpect(jsonPath("$.title").value("Input Validation Error"));
    }

    // --- PRUEBA 4: CRUD - Consultar por ID (Solo Admin/Analista) ---
    @Test
    void shouldGetApplicationByIdAsAdmin() throws Exception {
        // GIVEN: Insertamos manualmente un crédito en DB para consultarlo
        UserEntity savedUser = userRepository.findByUsername("juan.perez").orElseThrow();
        AffiliateEntity savedAffiliate = affiliateRepository.findByUser_Username("juan.perez").orElseThrow();

        CreditApplicationEntity credit = CreditApplicationEntity.builder()
                .affiliate(savedAffiliate)
                .amount(new BigDecimal("200000"))
                .termMonths(24)
                .status(CreditApplicationEntity.Status.PENDING)
                .build();

        CreditApplicationEntity savedCredit = creditApplicationRepository.save(credit);

        // WHEN & THEN: Consultamos como ADMIN
        mockMvc.perform(get("/api/v1/credits/" + savedCredit.getId())
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(200000.0));
    }

    // --- PRUEBA 5: Seguridad - Afiliado intentando ver ID (Forbidden) ---
    @Test
    void shouldDenyAccessToGetByIdForAffiliate() throws Exception {
        // Los afiliados no pueden consultar por ID directamente, solo admin/analista
        mockMvc.perform(get("/api/v1/credits/1")
                        .header("Authorization", affiliateToken))
                .andExpect(status().isForbidden());
    }
}