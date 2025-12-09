package com.coopcredit.creditapplication.application.dto.credit;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreditRequest {

    @NotNull(message = "Credit amount is required")
    @Positive(message = "Credit amount must be positive")
    @Min(value = 100000, message = "Minimum credit amount is 100,000")
    @Max(value = 100000000, message = "Maximum credit amount is 100,000,000")
    private BigDecimal amount;

    @NotNull(message = "Term months is required")
    @Min(value = 6, message = "Minimum term is 6 months")
    @Max(value = 72, message = "Maximum term is 72 months")
    private Integer termMonths;
}