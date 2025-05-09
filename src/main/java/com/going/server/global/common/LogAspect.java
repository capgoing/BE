package com.going.server.global.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Slf4j
@Component
public class LogAspect {

    private final ObjectMapper objectMapper;

    public LogAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Pointcut("execution(* com.going.server.domain..controller..*(..))")
    public void controllerMethods() {}

    @Pointcut("execution(* com.going.server.domain..service..*(..))")
    public void serviceMethods() {}

    @Around("controllerMethods()")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null || attributes.getRequest() == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        String uri = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        String httpMethod = request.getMethod();
        String controllerClass = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Map<String, Object> paramMap = getParams(request);

        try {
            log.info("HTTP {} {}", httpMethod, uri);
            log.info("Controller: {}.{}", controllerClass, methodName);
            log.info("Parameters: {}", objectMapper.writeValueAsString(paramMap));
        } catch (Exception e) {
            log.error("Failed to log controller {}.{}: {}", controllerClass, methodName, e.getMessage());
        }

        return joinPoint.proceed();
    }

    @Around("serviceMethods()")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info("Service executed: {}.{} ({} ms)", className, methodName, elapsedTime);
            return result;
        } catch (Throwable e) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.error("Exception in service: {}.{} ({} ms): {}", className, methodName, elapsedTime, e.getMessage());
            throw e;
        }
    }

    private static Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<>();

        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String param = paramNames.nextElement();
            paramMap.put(param.replaceAll("\\.", "-"), request.getParameter(param));
        }

        @SuppressWarnings("unchecked")
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathVariables != null) {
            for (Map.Entry<String, String> entry : pathVariables.entrySet()) {
                paramMap.put(entry.getKey().replaceAll("\\.", "-"), entry.getValue());
            }
        }

        return paramMap;
    }
}
