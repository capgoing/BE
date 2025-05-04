package com.going.server.domain.openai.service;

import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageCreateService {

    @Resource(name = "getOpenAIService")
    private final OpenAiService openAiService;

    public String generatePicture(String prompt) {
        CreateImageRequest createImageRequest = CreateImageRequest.builder()
                .prompt(prompt)
                .size("512x512") //사이즈
                .n(1)
                .build();

        //URL로 리턴 (1시간 후 만료)
        return openAiService.createImage(createImageRequest).getData().get(0).getUrl();
    }
}