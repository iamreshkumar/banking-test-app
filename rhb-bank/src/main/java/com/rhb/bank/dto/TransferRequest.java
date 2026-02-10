package com.rhb.bank.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequest {

   // 🔹 SOURCE
    @NotNull
    private TransferChannel fromChannel; // ACCOUNT / UPI

    private String fromAccountNumber;

    @Pattern(
            regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z]+$",
            message = "Invalid UPI ID"
    )
    private String fromUpiId;

    // 🔹 DESTINATION
    @NotNull
    private TransferChannel toChannel;

    private String toAccountNumber;

    @Pattern(
            regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z]+$",
            message = "Invalid UPI ID"
    )
    private String toUpiId;

    // 🔹 AMOUNT
    @NotNull
    private BigDecimal amount;

    // 🔹 UPI / BANK REFERENCE
    private String utr; // generated or received

    private String remarks;

    private TransferType transferType;
}
