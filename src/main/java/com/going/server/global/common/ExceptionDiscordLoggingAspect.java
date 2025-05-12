package com.going.server.global.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Aspect
@Component
public class ExceptionDiscordLoggingAspect {

    @Value("${discord.webhook.error-url}")
    private String webhookUrl;

    private final ObjectMapper objectMapper;
    private final Environment environment;
    private final DiscordNotifier discordNotifier;

    public ExceptionDiscordLoggingAspect(ObjectMapper objectMapper,
                                         Environment environment,
                                         DiscordNotifier discordNotifier) {
        this.objectMapper = objectMapper;
        this.environment = environment;
        this.discordNotifier = discordNotifier;
    }

    private boolean isNotificationDisabled() {
        return environment.acceptsProfiles("dev", "test");
    }

    private boolean shouldSkipNotification(Throwable ex) {
        SkipNotification skipNotification = ex.getClass().getAnnotation(SkipNotification.class);
        return skipNotification != null && skipNotification.value();
    }

    @AfterThrowing(
            pointcut = "(@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Service))",
            throwing = "ex"
    )
    public void handleException(JoinPoint joinPoint, Throwable ex) {
        if (isNotificationDisabled() || shouldSkipNotification(ex)) {
            return;
        }

        try {
            List<Map<String, Object>> fields = buildFields(joinPoint, ex);
            Map<String, Object> embed = Map.of("title", "서버 예외 발생", "color", 16711680, "fields", fields);
            Map<String, Object> payload = Map.of("embeds", List.of(embed));
            String fullStackTrace = buildStackTrace(ex);
            discordNotifier.send(webhookUrl, objectMapper.writeValueAsString(payload), fullStackTrace);
        } catch (Exception e) {
            log.warn("디스코드 예외 전송 실패: {}", e.getMessage());
        }
    }

    private List<Map<String, Object>> buildFields(JoinPoint joinPoint, Throwable ex) {
        List<Map<String, Object>> fields = new ArrayList<>();
        fields.add(Map.of("name", "Exception", "value", ex.getClass().getName(), "inline", false));
        fields.add(Map.of("name", "Message", "value", ex.getMessage() != null ? ex.getMessage() : "No message", "inline", false));
        fields.add(Map.of("name", "Location", "value", joinPoint.getSignature().toShortString(), "inline", false));
        fields.add(Map.of("name", "Time", "value", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "inline", false));
        return fields;
    }

    private String buildStackTrace(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}

