package com.coopcredit.creditapplication.infrastructure.adapter.input.rest;

import com.coopcredit.creditapplication.application.dto.credit.CreditRequest;
import com.coopcredit.creditapplication.application.dto.response.CreditApplicationResponse;
import com.coopcredit.creditapplication.application.mapper.CreditApplicationMapper;
import com.coopcredit.creditapplication.domain.exception.BusinessException; // Usaremos esto para denegar acceso
import com.coopcredit.creditapplication.domain.model.CreditApplication;
import com.coopcredit.creditapplication.domain.model.CreditApplicationStatus;
import com.coopcredit.creditapplication.domain.ports.input.ManageCreditApplicationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/credits")
@RequiredArgsConstructor
public class CreditApplicationController {

    private final ManageCreditApplicationUseCase manageCreditApplicationUseCase;
    private final CreditApplicationMapper mapper;

    @GetMapping
    public ResponseEntity<List<CreditApplicationResponse>> getAll(Authentication authentication) {
        String username = authentication.getName();
        var authorities = authentication.getAuthorities();

        List<CreditApplication> applications;

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {

            applications = manageCreditApplicationUseCase.getAllApplications();
        }
        else if (authorities.contains(new SimpleGrantedAuthority("ROLE_ANALISTA"))) {

            applications = manageCreditApplicationUseCase.getApplicationsByStatus(CreditApplicationStatus.PENDING);
        }
        else {
            applications = manageCreditApplicationUseCase.getApplicationsByAffiliate(username);
        }

        return ResponseEntity.ok(applications.stream()
                .map(mapper::toResponse)
                .toList());
    }

    @PostMapping
    public ResponseEntity<CreditApplicationResponse> requestCredit(
            @Valid @RequestBody CreditRequest request,
            Authentication authentication
    ) {
        String username = authentication.getName();
        var domainApplication = manageCreditApplicationUseCase.createApplication(
                username,
                request.getAmount(),
                request.getTermMonths()
        );
        return ResponseEntity.ok(mapper.toResponse(domainApplication));
    }


    @GetMapping("/{id}")
    public ResponseEntity<CreditApplicationResponse> getById(@PathVariable Long id, Authentication authentication) {
        var app = manageCreditApplicationUseCase.getApplicationById(id);

        boolean isAdminOrAnalyst = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_ANALISTA"));

        String ownerUsername = app.getAffiliate().getUser().getUsername();

        if (!isAdminOrAnalyst && !ownerUsername.equals(authentication.getName())) {
            throw new BusinessException("Access Denied: You can only view your own applications.");
        }

        return ResponseEntity.ok(mapper.toResponse(app));
    }
}