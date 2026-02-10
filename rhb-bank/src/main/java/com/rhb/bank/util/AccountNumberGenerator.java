package com.rhb.bank.util;

import org.springframework.stereotype.Component;

@Component
public class AccountNumberGenerator {

    public String generate() {
        return "RBH" + System.currentTimeMillis();
    }
}
