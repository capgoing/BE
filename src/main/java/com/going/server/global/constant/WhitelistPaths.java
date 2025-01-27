package com.going.server.global.constant;

public class WhitelistPaths {
    public static final String[] WHITELIST = {
            "/api/test", // 통신 테스트

            // Swagger
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/v3/api-docs/swagger-config"
    };
}
