package com.going.server.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI OpenAPI() {
        Info info = new Info()
            .title("Test SpringBoot API")
            .description("<h3>CapGoing API 명세서</h3>")
            .version("1.0.0");

        return new OpenAPI()
            .info(info);
    }
}
