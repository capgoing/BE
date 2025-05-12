package com.going.server.global.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class DiscordNotifier {

    private final RestTemplate restTemplate;

    public DiscordNotifier(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Async
    public void send(String webhookUrl, String payloadJson, String fullStackTrace) {
        try {
            byte[] traceBytes = fullStackTrace.getBytes(StandardCharsets.UTF_8);
            ByteArrayResource traceFile = new TraceFileResource(traceBytes, "stacktrace.txt");

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("payload_json", payloadJson);
            body.add("file", traceFile);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(webhookUrl, entity, String.class);
            log.info("디스코드 응답: {}", response.getStatusCode());

        } catch (Exception e) {
            log.error("디스코드 전송 실패: {}", e.getMessage(), e);
        }
    }
}

