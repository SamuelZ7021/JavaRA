package com.coopcredit.creditapplication.domain.ports.input;

import com.coopcredit.creditapplication.domain.model.CreditApplication;
import java.math.BigDecimal;
import java.util.List;

public interface ManageCreditApplicationUseCase {

    CreditApplication createApplication(String username, BigDecimal amount, Integer termMonths);

    CreditApplication getApplicationById(Long id);

    List<CreditApplication> getApplicationsByAffiliate(String username);

    List<CreditApplication> getAllApplications(); // Para Analistas/Admin
}