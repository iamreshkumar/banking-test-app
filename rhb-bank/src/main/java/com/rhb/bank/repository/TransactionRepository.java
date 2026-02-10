package com.rhb.bank.repository;

import com.rhb.bank.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByAccount_AccountId(Long accountId, Pageable pageable);

    Page<Transaction> findByAccount_AccountNumber(String accountNumber, PageRequest transactionDate);
}
