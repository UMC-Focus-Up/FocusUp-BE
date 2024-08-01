package com.focusup.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfig {
    private static final String ACCESS_TOKEN_KEY = "Access Token (Bearer)";
    private static final String REFRESH_TOKEN_KEY = "Refresh Token";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(createComponents())
                .addSecurityItem(createSecurityRequirement())
                .info(createApiInfo());
    }

    private Components createComponents() {
        return new Components()
                .addSecuritySchemes(ACCESS_TOKEN_KEY, createAccessTokenSecurityScheme())
                .addSecuritySchemes(REFRESH_TOKEN_KEY, createRefreshTokenSecurityScheme());
    }

    private SecurityRequirement createSecurityRequirement() {
        return new SecurityRequirement()
                .addList(ACCESS_TOKEN_KEY)
                .addList(REFRESH_TOKEN_KEY);
    }

    private SecurityScheme createAccessTokenSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("Authorization")
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);
    }

    private SecurityScheme createRefreshTokenSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("refresh token");
    }

    private Info createApiInfo() {
        return new Info()
                .title("FocusUp API")
                .description("FocusUp API 명세서")
                .version("1.0.0");
    }
}
