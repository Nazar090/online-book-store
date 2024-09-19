package com.example.onlinebookstore.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    private static final String bearerAuth = "BearerAuth";
    private static final String bearer = "bearer";
    private static final String jwt = "JWT";

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(bearerAuth,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme(bearer)
                                .bearerFormat(jwt)))
                .addSecurityItem(new SecurityRequirement().addList(bearerAuth));
    }
}
