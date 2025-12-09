package com.coopcredit.creditapplication.application.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AffiliateResponse {
    private Long id;
    private String fullName;
    private String email;
    private String address;
    private BigDecimal salary;
}