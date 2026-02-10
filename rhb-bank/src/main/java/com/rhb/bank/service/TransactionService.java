package com.rhb.bank.service;

import com.rhb.bank.dto.TransferRequest;
import com.rhb.bank.util.ApiResponse;

public interface TransactionService {
    ApiResponse doTransaction(TransferRequest request);

    ApiResponse getTransactions(Long accountId, int page, int size);

}
