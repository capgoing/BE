package com.going.server.global.config;

import org.springframework.context.annotation.Configuration;
import com.theokanning.openai.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@Configuration
public class OpenAIConfig {
    @Value("${openai.key}")
    private String apiKey;

    @Bean
    public OpenAiService getOpenAIService() {
        return new OpenAiService(apiKey, Duration.ofSeconds(30));
    }
}
