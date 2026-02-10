package com.rhb.bank.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class OpenAccountDTO {

    @NotBlank(message = "Customer name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid mobile number format"
    )
    private String mobileNumber;

    @NotNull(message = "Opening balance is required")
    @PositiveOrZero(message = "Opening balance cannot be negative")
    private BigDecimal openingBalance;

    @NotNull(message = "accountType is required")
    private String accountType;

    @NotBlank(message = "PAN number is required")
    @Pattern(
            regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$",
            message = "Invalid PAN format"
    )
    private String panNumber;

    private Boolean forceAccountCreation;

}
