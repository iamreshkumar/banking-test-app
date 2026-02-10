package com.rhb.bank.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private BigDecimal amount;

    private String type; // CREDIT / DEBIT

    private LocalDateTime transactionDate;

    private String utr;

    private String remarks;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
