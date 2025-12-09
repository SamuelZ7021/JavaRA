package com.coopcredit.creditapplication.infrastructure.adapter.input.rest;

import com.coopcredit.creditapplication.application.dto.credit.CreditRequest;
import com.coopcredit.creditapplication.application.dto.response.CreditApplicationResponse;
import com.coopcredit.creditapplication.application.mapper.CreditApplicationMapper;
import com.coopcredit.creditapplication.domain.model.CreditApplication;
import com.coopcredit.creditapplication.domain.ports.input.ManageCreditApplicationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/credits")
@RequiredArgsConstructor
public class CreditApplicationController {

    private final ManageCreditApplicationUseCase manageCreditApplicationUseCase;
    private final CreditApplicationMapper mapper; // Inyectar Mapper

    @PostMapping
    @PreAuthorize("hasRole('ROLE_AFILIADO')")
    public ResponseEntity<CreditApplicationResponse> requestCredit(
            @Valid @RequestBody CreditRequest request, // @Valid agregado
            Authentication authentication
    ) {
        String username = authentication.getName();

        // 1. Ejecutar caso de uso (Retorna Dominio)
        var domainApplication = manageCreditApplicationUseCase.createApplication(
                username,
                request.getAmount(),
                request.getTermMonths()
        );

        // 2. Mapear a DTO de Respuesta (Capa de Presentación)
        return ResponseEntity.ok(mapper.toResponse(domainApplication));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ANALISTA')")
    public ResponseEntity<CreditApplicationResponse> getById(@PathVariable Long id) {
        var domainApplication = manageCreditApplicationUseCase.getApplicationById(id);
        return ResponseEntity.ok(mapper.toResponse(domainApplication));
    }
}