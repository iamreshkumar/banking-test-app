package com.rhb.bank.repository;

import com.rhb.bank.entity.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.accountNumber = :accNo")
    Optional<Account> lockAccount(@Param("accNo") String accNo);

    List<Account> findByCustomer_CustomerId(Long customerId);

    Optional<Account> findByAccountNumber(String accountNumber);
}
