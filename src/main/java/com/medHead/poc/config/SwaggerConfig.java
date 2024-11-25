package com.medHead.poc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration pour Swagger/OpenAPI.
 * Cette classe configure l'intégration d'OpenAPI (Swagger) dans l'application.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Définit la configuration personnalisée pour OpenAPI/Swagger.
     * @return Une instance configurée de OpenAPI.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Documentation")
                        .version("1.0")
                        .description("Documentation pour l'API MedHead"));
    }
}
