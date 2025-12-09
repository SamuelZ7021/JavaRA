package com.coopcredit.creditapplication.application.dto.request; // Ajusta el paquete si es necesario

import jakarta.validation.constraints.Email;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateAffiliateRequest {
    private String address;

    @Email(message = "Invalid email format")
    private String email;

    private String fullName;
}