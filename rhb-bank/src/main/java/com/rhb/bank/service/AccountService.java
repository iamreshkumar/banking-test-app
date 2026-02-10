package com.rhb.bank.service;

import com.rhb.bank.dto.CloseAccountDTO;
import com.rhb.bank.dto.OpenAccountDTO;
import com.rhb.bank.util.ApiResponse;

public interface AccountService {
    ApiResponse openAccount(OpenAccountDTO payload);

    ApiResponse closeAccount(CloseAccountDTO payload);
}
