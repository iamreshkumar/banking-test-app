package com.rhb.bank.serviceImpl;

import com.rhb.bank.dto.AccountStatus;
import com.rhb.bank.dto.TransferRequest;
import com.rhb.bank.entity.Account;
import com.rhb.bank.entity.Transaction;
import com.rhb.bank.repository.AccountRepository;
import com.rhb.bank.repository.TransactionRepository;
import com.rhb.bank.service.TransactionService;
import com.rhb.bank.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository txRepo;
    private final AccountRepository accountRepo;

    public TransactionServiceImpl(TransactionRepository txRepo,
                                  AccountRepository accountRepo) {
        this.txRepo = txRepo;
        this.accountRepo = accountRepo;
    }

    @Override
    @Transactional
    public ApiResponse<String> doTransaction(TransferRequest request) {

        log.info("Transaction request received | from={} | to={} | amount={}",
                request.getFromAccountNumber(),
                request.getToAccountNumber(),
                request.getAmount());

        //  Validate request
        validateRequest(request);

        // Lock accounts (pessimistic locking for safety)
        var source = accountRepo.lockAccount(request.getFromAccountNumber())
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        var destination = accountRepo.lockAccount(request.getToAccountNumber())
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        //  Account status checks
        validateAccountStatus(source, destination);

        //  Balance check
        if (source.getBalance().compareTo(request.getAmount()) < 0) {
            log.warn("Insufficient balance | account={} | balance={}", source.getAccountNumber(), source.getBalance());
            throw new RuntimeException("Insufficient balance");
        }

        //  Perform debit & credit
        source.setBalance(source.getBalance().subtract(request.getAmount()));
        destination.setBalance(destination.getBalance().add(request.getAmount()));

        //  Persist transactions
        Transaction debitTx = buildTransaction(source, request.getAmount(), request);

        Transaction creditTx = buildTransaction(destination, request.getAmount(),  request);

        txRepo.save(debitTx);
        txRepo.save(creditTx);

        accountRepo.save(source);
        accountRepo.save(destination);

        log.info("Transaction successful | from={} | to={} | amount={} | utr={}",
                source.getAccountNumber(),
                destination.getAccountNumber(),
                request.getAmount(),
                request.getUtr());


        return new ApiResponse<>(true, "Transaction completed successfully", request.getUtr(), HttpStatus.OK.value());

    }

    @Override
    public ApiResponse getTransactions(Long accountId, int page, int size) {
        log.info("Fetching transactions | accountId={} | page={} | size={}", accountId, page, size);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("transactionDate").descending());

        Page<Transaction> response = txRepo.findByAccount_AccountId(accountId, pageRequest);

        return new ApiResponse<>(true, "Fetched successfully", response, HttpStatus.OK.value());

    }

    private void validateRequest(TransferRequest request) {

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Transaction amount must be positive");
        }

        if (request.getFromAccountNumber().equals(request.getToAccountNumber())) {
            throw new RuntimeException("Source and destination account cannot be same");
        }
    }

    private void validateAccountStatus(Account source, Account destination) {

        if (source.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Source account is not active");
        }

        if (destination.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Destination account is not active");
        }
    }

    private Transaction buildTransaction(Account account,
                                         BigDecimal amount,
                                         TransferRequest request) {

        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setAmount(amount);
        tx.setType(request.getTransferType().toString());
        tx.setUtr(request.getUtr());
        tx.setRemarks(request.getRemarks());
        tx.setTransactionDate(LocalDateTime.now());
        return tx;
    }
}