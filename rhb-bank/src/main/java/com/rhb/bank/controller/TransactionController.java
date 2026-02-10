package com.rhb.bank.controller;

import com.rhb.bank.dto.TransferRequest;
import com.rhb.bank.service.TransactionService;
import com.rhb.bank.util.ApiResponse;
import com.rhb.bank.util.AppUrls;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions/")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }
    @PostMapping(AppUrls.INITIATE_TRANSACTION)
    public ApiResponse doTransaction(@RequestBody TransferRequest request) {
        return service.doTransaction(request);
    }

    @GetMapping(AppUrls.HISTORY)
    public ApiResponse history(
            @RequestParam Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return service.getTransactions(accountId, page, size);
    }
}
