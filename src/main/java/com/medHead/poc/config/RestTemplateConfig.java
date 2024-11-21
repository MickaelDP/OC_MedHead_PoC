package com.medHead.poc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;

/**
 * Configuration globale pour RestTemplate.
 */
@Configuration
public class RestTemplateConfig {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateConfig.class);

    @Bean
    public RestTemplate restTemplate() {
        try {
            // Désactiver la validation des certificats
            disableSslValidation();

            // Créer le RestTemplate avec une factory basique
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);

            // Ajout du gestionnaire d'erreurs personnalisé
            restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
                @Override
                public void handleError(ClientHttpResponse response) throws IOException {
                    String responseBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    logger.error("Erreur HTTP : {} - {} - Corps de réponse : {}", response.getStatusCode(), response.getStatusText(), responseBody);
                }
            });

            return restTemplate;
        } catch (Exception e) {
            logger.error("Erreur lors de la configuration du RestTemplate : {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Désactive la validation SSL pour accepter tous les certificats.
     */
    private void disableSslValidation() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                }
        };

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }
}

