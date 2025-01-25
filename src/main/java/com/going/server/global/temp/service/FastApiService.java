package com.going.server.global.temp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class FastApiService {
    @Value("${fastapi.base-url}")
    private String baseUrl;
    private final WebClient webClient;
    public FastApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }
    public String callFastApi() {
        return webClient.get()
                .uri(baseUrl+"/")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
