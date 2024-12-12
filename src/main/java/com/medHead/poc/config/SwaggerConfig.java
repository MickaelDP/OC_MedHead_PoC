package com.medHead.poc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration pour Swagger/OpenAPI.
 * Cette classe configure l'intégration d'OpenAPI (Swagger) dans l'application.
 */
@Configuration
public class SwaggerConfig {

    private static final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);
    private static final Marker APP_MARKER = MarkerFactory.getMarker("APP_FILE");


    /**
     * Définit la configuration personnalisée pour OpenAPI/Swagger.
     * @return Une instance configurée de OpenAPI.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        logger.info(APP_MARKER, "Configuration d'OpenAPI en cours.");

        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("API Documentation")
                        .version("1.0")
                        .description("Documentation pour l'API MedHead"));

        logger.info(APP_MARKER, "Configuration d'OpenAPI terminée avec succès.");

        return openAPI;
    }
}
