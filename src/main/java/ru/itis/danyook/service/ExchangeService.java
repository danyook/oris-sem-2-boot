package ru.itis.danyook.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itis.danyook.dto.ExchangeRateResponse;

@Service
@RequiredArgsConstructor
public class ExchangeService {

    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/USD";

    private final RestTemplate restTemplate;


    public String getExchangeRates() {
        ExchangeRateResponse response = restTemplate.getForObject(API_URL, ExchangeRateResponse.class);

        if (response != null && response.getRates() != null) {
            return response.getRates().toString();
        } else {
            throw new RuntimeException("Траблы с апишкой");
        }
    }
}
