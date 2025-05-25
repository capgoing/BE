package com.going.server.domain.openai.service;

import com.going.server.domain.openai.dto.ImageCreateRequestDto;
import com.going.server.domain.openai.dto.ImageCreateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ImageCreateService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String openAIImageUrl;
    private final String apiKey;

    public ImageCreateService(
            @Qualifier("openAIImageUrl") String openAIImageUrl,
            @Qualifier("openAIKey") String apiKey
    ) {
        this.openAIImageUrl = openAIImageUrl;
        this.apiKey = apiKey;
    }

    public String generatePicture(ImageCreateRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ImageCreateRequestDto> entity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<ImageCreateResponseDto> response = restTemplate.exchange(
                openAIImageUrl,
                HttpMethod.POST,
                entity,
                ImageCreateResponseDto.class
        );

        return response.getBody().getData().get(0).getUrl();
    }
}
