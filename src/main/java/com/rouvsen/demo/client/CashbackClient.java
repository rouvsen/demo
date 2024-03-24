package com.rouvsen.demo.client;

import com.rouvsen.demo.model.dto.CashbackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class CashbackClient {

    private final RestTemplate restTemplate;

    public CashbackResponse getCashbackAmount(Double transactionAmount) {
        String url = "https://cardzone-cashback-api-c2f5b8105e2b.herokuapp.com/api/cashback?transactionAmount={transactionAmount}";
        return restTemplate.getForObject(url, CashbackResponse.class, transactionAmount);
    }



}

