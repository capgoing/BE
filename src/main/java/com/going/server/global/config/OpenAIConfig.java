package com.going.server.global.config;

import com.theokanning.openai.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OpenAIConfig {

    @Value("${openai.key}")
    private String apiKey;

    @Value("${openai.image-url}")
    private String imageUrl;

    @Value("${openai.timeout:30}")
    private int timeout;

    @Bean
    public OpenAiService getOpenAIService() {
        return new OpenAiService(apiKey, Duration.ofSeconds(timeout));
    }

    @Bean(name = "openAIImageUrl")
    public String openAIImageUrl() {
        return imageUrl;
    }

    @Bean(name = "openAIKey")
    public String openAIKey() {
        return apiKey;
    }
}
