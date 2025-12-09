package com.coopcredit.creditapplication.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Affiliate {
    private Long id;
    private User user;
    private String fullName;
    private String email;
    private String address;
    private BigDecimal salary;
    private Boolean active;
}