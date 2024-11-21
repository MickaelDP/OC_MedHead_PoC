package com.medHead.poc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Configuration globale pour RestTemplate.
 */
@Configuration
public class RestTemplateConfig {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateConfig.class);


    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                // Lire le corps de la réponse
                String responseBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                // Ajoutez un log ou une gestion personnalisée
                logger.error("Erreur HTTP : {} - {} - Corps de réponse : {}", response.getStatusCode(), response.getStatusText(), responseBody);
            }
        });
        return restTemplate;
    }
}
