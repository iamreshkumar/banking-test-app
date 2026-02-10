package com.rhb.bank.serviceImpl;

import com.rhb.bank.entity.Transaction;
import com.rhb.bank.repository.AccountRepository;
import com.rhb.bank.repository.TransactionRepository;
import com.rhb.bank.service.PassbookService;
import com.rhb.bank.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PassbookServiceImpl implements PassbookService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public PassbookServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public ApiResponse<Page<Transaction>> getPassbook(
            String accountNumber, int page, int size) {

        log.info("Fetching passbook | accountNumber={} | page={} | size={}",
                accountNumber, page, size);

        // 1️⃣ Validate account
        accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // 2️⃣ Fetch paginated transactions
        Page<Transaction> transactions =
                transactionRepository.findByAccount_AccountNumber(
                        accountNumber,
                        PageRequest.of(
                                page,
                                size,
                                Sort.by("transactionDate").descending()
                        )
                );

        return new ApiResponse<>(true, "Passbook fetched successfully", transactions, HttpStatus.OK.value());

    }
}
