package com.rhb.bank.controller;

import com.rhb.bank.entity.Transaction;
import com.rhb.bank.service.PassbookService;
import com.rhb.bank.util.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/passbook")
public class PassbookController {

    private final PassbookService passbookService;

    public PassbookController(PassbookService passbookService) {
        this.passbookService = passbookService;
    }


    @GetMapping("/{accountNumber}")
    public ApiResponse<Page<Transaction>> getPassbook(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return passbookService.getPassbook(accountNumber, page, size);
    }


}
