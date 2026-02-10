package com.rhb.bank.service;

import com.rhb.bank.entity.Transaction;
import com.rhb.bank.util.ApiResponse;
import org.springframework.data.domain.Page;

public interface PassbookService {
    ApiResponse<Page<Transaction>> getPassbook(String accountNumber, int page, int size);
}
