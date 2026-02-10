package com.rhb.bank.controller;

import com.rhb.bank.util.AppUrls;
import com.rhb.bank.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
@Slf4j
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(AppUrls.LOGIN)
    public Map<String, String> login( @RequestParam String username, @RequestParam String role) {

        log.info("Login request for user={}, role={}", username, role);

        String token = jwtUtil.generateToken(username, role);

        return Map.of("token", token);
    }
}
