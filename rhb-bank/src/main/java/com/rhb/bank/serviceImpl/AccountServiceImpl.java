package com.rhb.bank.serviceImpl;

import com.rhb.bank.dto.AccountStatus;
import com.rhb.bank.dto.AccountType;
import com.rhb.bank.dto.CloseAccountDTO;
import com.rhb.bank.dto.OpenAccountDTO;
import com.rhb.bank.entity.Account;
import com.rhb.bank.entity.Customer;
import com.rhb.bank.repository.AccountRepository;
import com.rhb.bank.repository.CustomerRepository;
import com.rhb.bank.service.AccountService;
import com.rhb.bank.util.AccountNumberGenerator;
import com.rhb.bank.util.AccountRulesEngine;
import com.rhb.bank.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Amresh Kumar
 * <p>This service handles account related stuffs/p>
 */
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final AccountRulesEngine accountRulesEngine;

    public AccountServiceImpl(AccountRepository accountRepository, CustomerRepository customerRepository,
                              AccountNumberGenerator accountNumberGenerator, AccountRulesEngine accountRulesEngine) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.accountNumberGenerator = accountNumberGenerator;
        this.accountRulesEngine = accountRulesEngine;
    }

    @Transactional
    public ApiResponse openAccount(OpenAccountDTO payload) {

        log.info("Account opening request received | PAN={} | Mobile={} | Type={}",
                payload.getPanNumber(),
                payload.getMobileNumber(),
                payload.getAccountType());

        // PAN-based lookup
        Customer customer = customerRepository.findByPanNumber(payload.getPanNumber())
                .orElseGet(() -> {
                    log.info("No customer found for PAN={}, creating new customer",
                            payload.getPanNumber());
                    return createNewCustomer(payload);
                });

        List<Account> existingAccounts =
                accountRepository.findByCustomer_CustomerId(customer.getCustomerId());

        log.debug("Existing accounts count={} for customerId={}",
                existingAccounts.size(), customer.getCustomerId());

        // Banking rules
        accountRulesEngine.validateOpeningBalance(
                AccountType.valueOf(payload.getAccountType()),
                payload.getOpeningBalance()
        );

        // Existing account check
        if (!payload.getForceAccountCreation() && !existingAccounts.isEmpty()) {

            log.warn("Account creation blocked | customerId={} | existingAccounts={}",
                    customer.getCustomerId(),
                    existingAccounts.stream()
                            .map(Account::getAccountNumber)
                            .toList());

            return new ApiResponse<>(
                    false,
                    "Customer already has account(s): " +
                            existingAccounts.stream()
                                    .map(Account::getAccountNumber)
                                    .toList(),
                    null,
                    HttpStatus.BAD_REQUEST.value()
            );
        }

        // Create account
        Account account = new Account();
        account.setAccountNumber(accountNumberGenerator.generate());
        account.setBalance(payload.getOpeningBalance());
        account.setCustomer(customer);
        account.setStatus(AccountStatus.ACTIVE);
        account.setAccountType(AccountType.valueOf(payload.getAccountType()));

        accountRepository.save(account);

        log.info("Account opened successfully | accountNumber={} | customerId={} | type={}",
                account.getAccountNumber(),
                customer.getCustomerId(),
                account.getAccountType());

        return new ApiResponse<>(
                true,
                "Account opened successfully",
                account,
                HttpStatus.OK.value()
        );
    }

    private Customer createNewCustomer(OpenAccountDTO dto) {

        log.debug("Creating new customer | PAN={} | Email={}",
                dto.getPanNumber(), dto.getEmail());

        if (customerRepository.existsByMobileNumber(dto.getMobileNumber())) {
            log.error("Duplicate mobile detected | mobile={}", dto.getMobileNumber());
            throw new RuntimeException("Mobile number already registered");
        }

        if (customerRepository.existsByEmail(dto.getEmail())) {
            log.error("Duplicate email detected | email={}", dto.getEmail());
            throw new RuntimeException("Email already registered");
        }

        Customer customer = new Customer();
        customer.setFullName(dto.getFullName());
        customer.setEmail(dto.getEmail());
        customer.setMobileNumber(dto.getMobileNumber());
        customer.setPanNumber(dto.getPanNumber());

        Customer savedCustomer = customerRepository.save(customer);

        log.info("Customer created successfully | customerId={} | PAN={}",
                savedCustomer.getCustomerId(),
                savedCustomer.getPanNumber());

        return savedCustomer;
    }

    @Override
    public ApiResponse closeAccount(CloseAccountDTO payload) {

        log.info("Account closure request | accountNumber={}",
                payload.getAccountNumber());

        Account account = accountRepository.findByAccountNumber(payload.getAccountNumber())
                .orElseThrow(() -> {
                    log.error("Account not found | accountNumber={}",
                            payload.getAccountNumber());
                    return new RuntimeException("Account not found");
                });

        log.debug("Account status={} | balance={}",
                account.getStatus(), account.getBalance());

        if (account.getStatus() == AccountStatus.CLOSED) {
            log.warn("Account already closed | accountNumber={}",
                    payload.getAccountNumber());
            throw new RuntimeException("Account already closed");
        }

        if (account.getStatus() == AccountStatus.BLOCKED) {
            log.warn("Blocked account closure attempt | accountNumber={}",
                    payload.getAccountNumber());
            throw new RuntimeException("Blocked account cannot be closed");
        }

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            log.warn("Non-zero balance closure attempt | accountNumber={} | balance={}",
                    payload.getAccountNumber(),
                    account.getBalance());
            throw new RuntimeException(
                    "Account balance must be zero before closure"
            );
        }

        account.setStatus(AccountStatus.CLOSED);
        accountRepository.save(account);

        log.info("Account closed successfully | accountNumber={}",
                payload.getAccountNumber());

        return new ApiResponse<>(
                true,
                "Account closed successfully",
                account.getStatus(),
                HttpStatus.OK.value()
        );
    }
}