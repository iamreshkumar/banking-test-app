package com.rhb.bank.controller;

import com.rhb.bank.dto.CloseAccountDTO;
import com.rhb.bank.dto.OpenAccountDTO;
import com.rhb.bank.service.AccountService;
import com.rhb.bank.util.ApiResponse;
import com.rhb.bank.util.AppUrls;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts/")
public class AccountController {
    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    /**
     * <p>This api's belongs to create a new account into our banking system</p>
     *
     * @param payload
     * @return
     */
    @PostMapping(AppUrls.ACCOUNT_OPEN)
    public ApiResponse openAccount(@Valid @RequestBody OpenAccountDTO payload) {
        return service.openAccount(payload);
    }

    /**
     * <p>This api's belongs to close account into our banking system</p>
     *
     * @param payload
     * @return
     */
    @PostMapping(AppUrls.ACCOUNT_CLOSE)
    public ApiResponse<String> closeAccount(@Valid @RequestBody CloseAccountDTO payload) {
        return service.closeAccount(payload);
    }

}
