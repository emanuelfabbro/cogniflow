package com.cogniflow.fraud.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionRequest {

    @NotNull(message = "Account ID is required")
    private UUID accountId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Currency is required")
    @Pattern(regexp = "USD|EUR|ARS", message = "Currency must be USD, EUR, or ARS")
    private String currency;
}