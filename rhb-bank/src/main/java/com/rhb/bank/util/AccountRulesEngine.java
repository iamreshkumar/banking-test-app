package com.rhb.bank.util;

import com.rhb.bank.dto.AccountType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountRulesEngine {

    public void validateOpeningBalance(AccountType type, BigDecimal amount) {

        switch (type) {
            case SAVINGS -> {
                if (amount.compareTo(BigDecimal.valueOf(1000)) < 0)
                    throw new RuntimeException("Minimum balance for SAVINGS is 1000");
            }
            case CURRENT -> {
                if (amount.compareTo(BigDecimal.valueOf(10000)) < 0)
                    throw new RuntimeException("Minimum balance for CURRENT is 10000");
            }
            case SALARY -> {
                // No minimum balance
            }
            case FIXED_DEPOSIT -> {
                if (amount.compareTo(BigDecimal.valueOf(50000)) < 0)
                    throw new RuntimeException("Minimum FD amount is 50000");
            }
        }
    }
}
