package com.rhb.bank.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ExternalService {



    public String callGoogle() {
        RestTemplate restTemplate = new RestTemplate();
        log.info("Calling external API: Google");
        return restTemplate.getForObject(
                "https://www.google.com",
                String.class
        );
    }
}
